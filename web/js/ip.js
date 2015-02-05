$(function () {
        $('#table_ip').datagrid({
            url: basrUrl + "/findIp.do",
            rownumbers: true,
            fit: true,
            singleSelect: true,
            border: false,
            pagination: true,
            pageSize: 40,
            pageList: [40, 50, 60, 70],
            striped: true,
            columns: [[
                {field: 'ip', title: 'ip', width: 100, align: 'center'},
                {field: 'country', title: '国家', width: 100, align: 'center'},
                {field: 'province', title: '省份', width: 100, align: 'center'},
                {field: 'city', title: '城市', width: 100, align: 'center'},
                {
                    field: 'time',
                    title: '日期',
                    width: 100,
                    align: 'center',
                    formatter: function (value, rowData, rowIndex) {
                        value = value + "";
                        return value.substr(0, 4) + "-" + value.substr(4, 2) + "-" + value.substr(6, 2);
                    }
                }, {
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