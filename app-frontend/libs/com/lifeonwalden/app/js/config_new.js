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

    const globalConfig = {
        templateClassNameMap,
        dxDataGridDefaultConfig
    };
    $.getGlobalConfig = function() {
        return globalConfig || {};
    };

    // 常用api
    $.Api = function (host) {
        this.host = host;
    }
    $.Api.prototype.fetchData = function (api, params) {
        var _this = this;
        var _params;
        if (params) {
            _params = JSON.parse(JSON.stringify(params));
            var _keys = Object.keys(_params);
            _keys.forEach(_key => {
                if (_params[_key] === '' || _params[_key] === undefined || _params[_key] === null || _params[_key] === '*') {
                    delete _params[_key];
                }
            });
        }
        return $.ajax({
            url: _this.host + api,
            data: _params
        });
    }
    $.Api.prototype.pushData = function (api, params) {
        var _this = this;
        return $.ajax({
            url: _this.host + api,
            method: 'POST',
            data: params
        });
    }
    $.Api.prototype.fetchSystemSort = function () {
        return this.fetchData('appSystem/query');
    }
    $.Api.prototype.fetchTrigger = function () {
        return this.fetchData('trigger/query');
    }
})(jQuery);