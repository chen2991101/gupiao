$(function () {
        $('#market_table').datagrid({
            url: basrUrl + "/market/findRecords.do",
            rownumbers: true,
            pagination: true,
            fit: true,
            singleSelect: true,
            border: false,
            pageSize: 40,
            pageList: [40, 50, 60, 70],
            striped: true,
            columns: [[
                {field: 'no', title: '代码', width: 100, align: 'center'},
                {field: 'name', title: '名称', width: 100, align: 'center'},
                {field: 'currentPrice', title: '当前价格', width: 100, align: 'center'},
                {field: 'yesterday_income', title: '昨收', width: 100, align: 'center'},
                {field: 'today_open', title: '今开', width: 100, align: 'center'},
                {field: 'deal', title: '交易量', width: 100, align: 'center'},
                {field: 'out_dish', title: '外盘', width: 100, align: 'center'},
                {field: 'in_dish', title: '内盘', width: 100, align: 'center'},
                {field: 'dealAmount', title: '成交额', width: 100, align: 'center'},
                {field: 'handover', title: '换手率', width: 100, align: 'center'},
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