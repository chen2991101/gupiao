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
            rowStyler: function (index, row) {
                if (row.upanddown > 0) {
                    return 'color:red;';
                } else if (row.upanddown < 0) {
                    return 'color:green;';
                } else {
                    return 'color:black;';
                }
            },
            striped: true,
            columns: [[
                {field: 'no', title: '代码', width: 100, align: 'center'},
                {field: 'name', title: '名称', width: 100, align: 'center'},
                {field: 'price', title: '价格', width: 100, align: 'center'},
                {field: 'upanddown', title: '涨幅', width: 100, align: 'center'},
                {field: 'upanddown2', title: '涨幅(%)', width: 100, align: 'center'},
                {field: 'open', title: '开盘价', width: 100, align: 'center'},
                {field: 'highest', title: '最高', width: 100, align: 'center'},
                {field: 'lowest', title: '最低', width: 100, align: 'center'},
                {field: 'deal', title: '交易量', width: 100, align: 'center'},
                {field: 'dealMoney', title: '交易金额', width: 100, align: 'center'},
                {field: 'in_dish', title: '内盘', width: 100, align: 'center'},
                {field: 'out_dish', title: '外盘', width: 100, align: 'center'},
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