$(function () {
        $('#table').datagrid({
            url: basrUrl + "/address/find.do",
            rownumbers: true,
            pagination: true,
            fit: true,
            singleSelect: true,
            columns: [[
                {field: 'address', title: '地址', width: 200, align: 'center'},
                {field: 'latitude', title: '纬度', width: 100, align: 'center'},
                {field: 'longitude', title: '经度', width: 100, align: 'center'},
                {field: 'radius', title: '精度', width: 100, align: 'center'},
                {field: 'model', title: '手机型号', width: 100, align: 'center'},
                {field: 'deviceId', title: '设备ID', width: 100, align: 'center'},
                {
                    field: 'createDate',
                    title: '时间',
                    width: 150,
                    align: 'center',
                    formatter: function (value, rowData, rowIndex) {
                        return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    }
                }
            ]]
        });
    }
)

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