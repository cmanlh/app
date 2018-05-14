;(function ($) {
    // js path map
    const jsPathMap = {
        'form1': './js/form/form1.js',
        'form2': './js/form/form2.js',
        'form3': './js/form/form3.js',
        'form4': './js/form/form4.js',
    };
    // 模板文件功能区域对应的类名
    const templateClassNameSetting = {
        conditionHtmlClassName: 'toolbar-left',   //toolbar左侧
        controlHtmlClassName: 'toolbar-right',    //toobar右侧
        contentHtmlClassName: 'content'           //自定义内容
    };
    // dxDataGrid默认配置
    const dxDataGridDefaultConfig = {
        dataSource: [],
        height: window.innerHeight - 115,
        filterRow: {
            visible: true,
        },
        headerFilter: {
            visible: true,
        },
        hoverStateEnabled: true,
        rowAlternationEnabled: true,
        selection: {
            mode: 'single'
        },
        columnAutoWidth: true,
        scrolling: {
            mode: 'infinite'
        },
        columns: [{
            caption: '序号',
            fixed: true,
            width: 70,
            alignment: 'center',
            cellTemplate: function (box, data) {
                box.text(data.rowIndex + 1);
            }
        }]
    };
    $.getJsPathMap = function () {
        return jsPathMap || {};
    }
    $.getTempLatePathMap = function () {
        return templatePathMap || {};
    };
    $.getTemplateClassNameSetting = function () {
        return templateClassNameSetting || {};
    };
    $.getDxDataGridDefaultConfig = function () {
        return dxDataGridDefaultConfig || {};
    };
})(jQuery);