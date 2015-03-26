var basrUrl = 'http://127.0.0.1:8081';
//var basrUrl = 'http://haogupiao.duapp.com';


Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function progress_open() {
    // 打开进度条
    $.messager.progress({
        title: '提示',
        text: '努力操作中，请稍后......'
    });
};

function progress_close() {
    // 关闭进度条
    $.messager.progress('close');
}

function success_show(msg) {
    $.messager.show({
        title: '提示',
        msg: msg,
        style: {
            right: '',
            top: document.body.scrollTop + document.documentElement.scrollTop,
            bottom: ''
        }
    });
}

// json对象转换成字符串
function obj2str(o) {
    var r = [];
    if (typeof o == "string")
        return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
    if (typeof o == "undefined")
        return "undefined";
    if (typeof o == "object") {
        if (o === null)
            return "null";
        else if (!o.sort) {
            for (var i in o)
                r.push(i + ":" + obj2str(o[i]));
            r = "{" + r.join() + "}";
        } else {
            for (var i = 0; i < o.length; i++)
                r.push(obj2str(o[i]));
            r = "[" + r.join() + "]";
        }
        return r;
    }
    return o.toString();
}

function addTabs(id, title, href) {
    var tab = $('#' + id);
    if (tab.tabs('exists', title)) {
        tab.tabs('select', title);
    } else {
        tab.tabs('add', {
            title: title,
            closable: 'true',
            cache: true,
            href: href
        });
    }
}

function selectOnlyOne(grid) {
    var rows = grid.datagrid('getSelections');
    if (rows.length != 1) {
        $.messager.alert('提示', '只能选择一条数据', 'warning');
        return null;
    }
    return rows[0];
}

function mustSelect(grid) {
    var rows = grid.datagrid('getSelections');// 获得表格选中的行
    if (rows.length == 0) {
        $.messager.alert('提示', '请至少选择一条数据', 'warning');
        return null;
    }
    return rows;
}

function updateRow(gridId, newRow) {
    // 修改数据后直接修改grid上面的数据，不连接数据库
    var grid = $('#' + gridId);
    var row = grid.datagrid('getSelected');// 当前选中的行
    var index = grid.datagrid('getRowIndex', row);
    grid.datagrid('updateRow', {
        index: index,
        row: newRow
    });
}

function submitForm(gridId, form, url, m, dialog) {
    // 提交表单
    form.form('submit', {
        url: url,
        onSubmit: function () {
            progress_open();
            var isValid = $(this).form('validate');
            if (!isValid) {
                progress_close();
            }
            return isValid;
        },
        success: function (data) {
            progress_close();
            data = $.parseJSON(data);
            if (data.success) {
                success_show('操作成功');
                if (m == 'add') {
                    // 如果是添加需要清空表单继续添加
                    form.form('reset');
                } else {
                    // 修改需要关闭表单
                    dialog.dialog('close');
                }
                $('#' + gridId).datagrid('reload');
            } else {
                $.messager.alert('提示', data.msg, 'error');
            }
        }
    });
}

function createCode() {
    // 日期加4位随机数
    var random = new Date().Format('yyyyMMddhhmmss');
    var j;
    for (var i = 0; i < 4; i++) {
        j = Math.random() * 10;
        random += Math.ceil(j > 9 ? 0 : j);
    }
    return random;
}

function createRelateUnitsCombogrid(id) {
    // 创建往来单位的combogrid
    $('#' + id).combogrid({ // 往来单位
        url: 'basic/relateUnits/findByCategoryId.do',
        idField: 'id',
        textField: 'relateUnitsName',
        panelWidth: 450,
        validType: "combogrid['" + id + "']",
        loadMsg: '正在处理，请稍待。。。',
        mode: 'remote',
        delay: 500,
        pagination: true,
        fitColumns: true,
        columns: [[{
            field: 'relateUnitsName',
            title: '往来单位',
            width: 100,
            align: 'center'
        }, {
            field: 'contacts',
            title: '联系人',
            width: 100,
            align: 'center'
        }, {
            field: 'phone',
            title: '电话',
            width: 100,
            align: 'center'
        }, {
            field: 'address',
            title: '地址',
            width: 100,
            align: 'center'
        }]],
        onShowPanel: function () {
            // 展开下拉框时加载数据
            var grid = $(this).combogrid('grid');
            var q = '';
            if (grid.datagrid('getRows').length > 0) {
                // 如果下拉框里面有数据就不刷新
                q = null;
            }
            grid.datagrid('load', {
                q: q
            });
        },
        onBeforeLoad: function (param) {
            if (param.q == null) {
                return false;
            }
        }
    });
}

function createWarehouseCombogrid(id) {
    // 仓库
    $('#' + id).combogrid({ // 往来单位
        url: 'basic/warehouse/find.do',
        idField: 'id',
        textField: 'warehouseName',
        panelWidth: 450,
        validType: "combogrid['" + id + "']",
        loadMsg: '正在处理，请稍待。。。',
        mode: 'remote',
        delay: 500,
        pagination: true,
        fitColumns: true,
        columns: [[{
            field: 'warehouseName',
            title: '仓库名称',
            width: 100,
            align: 'center'
        }, {
            field: 'address',
            title: '仓库地址',
            width: 100,
            align: 'center'
        }, {
            field: 'remark',
            title: '备注',
            width: 100,
            align: 'center'
        }]],
        onShowPanel: function () {
            // 展开下拉框时加载数据
            var grid = $(this).combogrid('grid');
            var q = '';
            if (grid.datagrid('getRows').length > 0) {
                // 如果下拉框里面有数据就不刷新
                q = null;
            }
            grid.datagrid('load', {
                q: q
            });
        },
        onBeforeLoad: function (param) {
            if (param.q == null) {
                return false;
            }
        }
    });
}

function createHandlerCombogrid(id) {
    // 创建经手人
    $('#' + id).combogrid({ // 往来单位
        url: 'system/findUsers.do',
        idField: 'id',
        textField: 'name',
        panelWidth: 450,
        validType: "combogrid['" + id + "']",
        loadMsg: '正在处理，请稍待。。。',
        mode: 'remote',
        delay: 500,
        pagination: true,
        fitColumns: true,
        columns: [[{
            field: 'name',
            title: '姓名',
            width: 100,
            align: 'center'
        }, {
            field: 'sex',
            title: '性别',
            width: 50,
            formatter: function (value, row, index) {
                return value == true ? '男' : '女';
            },
            align: 'center'
        }, {
            field: 'phone',
            title: '电话',
            width: 100,
            align: 'center'
        }]],
        onShowPanel: function () {
            // 展开下拉框时加载数据
            var grid = $(this).combogrid('grid');
            var q = '';
            if (grid.datagrid('getRows').length > 0) {
                // 如果下拉框里面有数据就不刷新
                q = null;
            }
            grid.datagrid('load', {
                q: q
            });
        },
        onBeforeLoad: function (param) {
            if (param.q == null) {
                return false;
            }
        }
    });
}

function update_fund_grid(id, m) {
    // 更新账户信息的全局变量
    $.each(_fund_grid, function () {
        if (this.id == id) {
            this.money -= m;
            return false;
        }
    });
}

function advanced_query(gridId, id) {
    // 高级查询
    var b = $('#' + id + '_form').form('validate');
    if (b) {
        $('#' + gridId).datagrid('load', {
            init: 'init',
            query: $('#' + id + '_query').val(),
            beginTime: $('#' + id + '_beginTime').datebox('getValue'),
            endTime: $('#' + id + '_endTime').datebox('getValue'),
            relateUnitsId: $('#' + id + '_relateUnitsId').combogrid('getValue'),
            handlerId: $('#' + id + '_handler').combogrid('getValue')
        });
    }
}

function get_warehouse_grid() {
    if (_warehouse_grid == null) {
        // 同步获取仓库信息
        $.ajax({
            type: "POST",
            url: 'basic/warehouse/find.do',
            async: false,
            success: function (data) {
                _warehouse_grid = $.parseJSON(data);
            }
        });
    }
    return _warehouse_grid;
}