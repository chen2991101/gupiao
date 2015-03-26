$(function () {
    $('#market').bind('click', function () {
        addTabs('index_tab', '股票', '/view/market.html');
    });

    $('#macd').bind('click', function () {
        addTabs('index_tab', '金叉', '/view/macd.html');
    });

    $('#kdj').bind('click', function () {
        addTabs('index_tab', 'kdj', '/view/kdj.html');
    });

    $('#address').bind('click', function () {
        addTabs('index_tab', '地址', '/view/address.html');
    });
});