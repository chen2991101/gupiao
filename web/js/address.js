$(function () {
        $('#table').datagrid({
            url: basrUrl + "/address/find.do",
            rownumbers: true,
            pagination: true,
            fit: true,
            singleSelect: true,
            border: false,
            pageSize: 40,
            pageList: [40, 50, 60, 70],
            striped: true,
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