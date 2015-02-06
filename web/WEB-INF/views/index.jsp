<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>股市行情</title>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/easyUI/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/easyUI/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/easyUI/themes/icon.css">
    <script type="text/javascript" src="/easyUI/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/easyUI/locale/easyui-lang-zh_CN.js"></script>
    <script>
        $(function () {
            $('#tab_home').panel({
                href: '/view/home.html',
                border: false,
                fit: true
            });
        });
    </script>
</head>
<body>
<div class="easyui-layout" fit=true border=false>
    <div region="west" style="width: 200px;" border=false
         href='/view/left.html' split="true" title="菜单"></div>
    <div region="center" id="index_center"
         border=false>
        <div id="index_tab" class="easyui-tabs" fit=true border=false>
            <div title="首页" id="tab_home">
            </div>
        </div>
    </div>
</body>
</html>
