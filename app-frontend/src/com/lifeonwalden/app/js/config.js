;
(function($) {
    $.datetimepicker.setLocale('zh');
    // 模板文件功能区域对应的类名
    const templateClassNameMap = {
        conditionHtmlClassName: 'toolbar-left', //toolbar左侧
        controlHtmlClassName: 'toolbar-right', //toobar右侧
        contentHtmlClassName: 'content' //自定义内容
    };
    // dxDataGrid默认配置
    const dxDataGridDefaultConfig = {
        dataSource: [],
        height: window.innerHeight - 140,
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

    const globalConfig = {
        templateClassNameMap,
        dxDataGridDefaultConfig
    };
    $.getGlobalConfig = function() {
        return globalConfig || {};
    };
    // 时间格式化
    $.timeFormat = function (format='YYYY-MM-DD') {
        return function (value) {
            var _time = value
            if (typeof value == 'string' && !isNaN(+value)) {
                _time = +value;                
            }
            return moment(_time).format(format);
        }
    }
})(jQuery);