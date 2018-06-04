;
(function($) {
    // 模板文件功能区域对应的类名
    const templateClassNameMap = {
        conditionHtmlClassName: 'toolbar-left', //toolbar左侧
        controlHtmlClassName: 'toolbar-right', //toobar右侧
        contentHtmlClassName: 'content' //自定义内容
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
            cellTemplate: function(box, data) {
                box.text(data.rowIndex + 1);
            }
        }]
    };

    const commonDataApi = {
        system: 'http://172.29.114.86:8080/ops/appSystem/query',
        department: 'http://172.29.114.86:8080/ops/auth/role/queryDepartMentInfoCache',
    };

    const globalConfig = {
        templateClassNameMap,
        dxDataGridDefaultConfig,
        commonDataApi
    };
    $.getGlobalConfig = function() {
        return globalConfig || {};
    };
})(jQuery);