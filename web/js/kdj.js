$(function () {
        $('#table_kdj').datagrid({
            url: basrUrl + "/market/findKdjCross.do",
            rownumbers: true,
            fit: true,
            singleSelect: true,
            border: false,
            striped: true,
            columns: [[
                {field: 'no', title: '代码', width: 100, align: 'center'},
                {field: 'name', title: '名称', width: 100, align: 'center'},
                {field: 'k', title: 'k', width: 100, align: 'center'},
                {field: 'd', title: 'd', width: 100, align: 'center'},
                {field: 'j', title: 'j', width: 100, align: 'center'},
                {
                    field: 'time',
                    title: '时间',
                    width: 100,
                    align: 'center',
                    formatter: function (value, rowData, rowIndex) {
                        value = value + "";
                        return value.substr(0, 4) + "-" + value.substr(4, 2) + "-" + value.substr(6, 2);
                    }
                }
            ]]
        });
    }
)