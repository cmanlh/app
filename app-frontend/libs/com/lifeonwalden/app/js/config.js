;(function($) {
    $.datetimepicker.setLocale('zh');
    // 模板文件功能区域对应的类名
    const templateClassNameMap = {
        conditionHtmlClassName: 'toolbar-left', //toolbar左侧
        controlHtmlClassName: 'toolbar-right', //toobar右侧
        contentHtmlClassName: 'content' //自定义内容
    };
    // dxDataGrid默认配置
    const dxDataGridDefaultConfig = {        
        exportProxyClassName: 'export',
        searchProxyClassName: 'search',
        dataSource: [],
        height: window.innerHeight - 170,
        filterRow: {
            visible: true,
        },
        headerFilter: {
            visible: true,
        },
        hoverStateEnabled: true,
        rowAlternationEnabled: true,
        // selection: {
        //     mode: 'single'
        // },
        grouping: {
            expandMode: 'rowClick'
        },
        columnAutoWidth: true,
        scrolling: {
            mode: 'infinite'
        },
        showRowLines: true,
        showBorders: true,
        columns: [{
            caption: '序号',
            fixed: true,
            width: 70,
            cssClass: 'bgf5f6fa',
            alignment: 'center',
            cellTemplate: function(box, data) {
                box.text(data.rowIndex + 1);
            },
            allowExporting: false
        }],
        loadPanel: {
            enabled: false
        }
    };

    const globalConfig = {
        templateClassNameMap,
        dxDataGridDefaultConfig
    };
    $.getGlobalConfig = function() {
        return globalConfig || {};
    };
})(jQuery);