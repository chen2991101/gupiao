$(function () {
    $('#market').bind('click', function () {
        alert('easyui');
    });

    $('#address').bind('click', function () {
        addTabs('index_tab', '地址', '/view/address.html');
    });
});