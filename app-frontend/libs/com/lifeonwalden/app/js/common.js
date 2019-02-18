$JqcLoader.registerModule($JqcLoader.newModule('com.jquery', LIB_ROOT_PATH).registerComponents(['jquery', 'keycode', 'version']))
    .registerModule($JqcLoader.newModule('com.lifeonwalden.jqc', LIB_ROOT_PATH)
        .registerComponents(['baseElement', 'blocker', 'cache', 'dateUtil', 'dialog', 'draggable', 'format', 'inputNumber', 'lang'])
        .registerComponents(['loader', 'location', 'pinyin', 'resizeable', 'selectbox', 'slide', 'uniqueKey', 'slide', 'uniqueKey'])
        .registerComponents(['valHooks', 'zindex'])
        .registerComponents(['toolkit'])
        .registerComponents(['menuTree'])
        .registerComponents(['notification', 'tag', 'calendar', 'notify'])
        .registerComponents(['contextmenu','layoutHelper'])
        .registerComponents(['loading'])
        .registerComponents(['confirm'])
        .registerComponents(['event'])
        .registerComponents(['asyncSelect'])
        .registerComponents(['timepicker'])
        .registerComponents(['select'])
        .registerComponents(['icon'])
        .registerComponents(['upload'])
        .registerComponents(['nav'])    // 按需加载
        .registerComponents(['formToolBar', 'formUtil', 'datetimepicker', 'tip', 'msg', 'tab'])
        .registerComponents(['echarts']) //图表
        .registerComponents(['bpmn']) // bpmn2.0
        .registerComponents(['timeline']) //时间线
        .registerComponents(['jsoneditor']) //json编辑器图表
        .registerComponents(['editor']) //富文本编辑器
        .registerComponents(['checkbox'])
        .registerComponents(['apisBox']));

const COMP_LIB_PATH = 'com.lifeonwalden.jqc';

$JqcLoader.importComponents('com.jquery', ['jquery', 'keycode', 'version'])
    .importComponents('com.lifeonwalden.jqc', ['select', 'asyncSelect', 'confirm', 'checkbox', 'event', 'menuTree', 'formUtil', 'msg', 'tab', 'dialog', 'formToolBar', 'contextmenu', 'toolkit', 'loading','layoutHelper', 'notification', 'tag', 'calendar', 'icon', 'upload', 'notify'])
    // dx组件
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/jszip.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.web.debug.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.messages.cn.js'))
    .importCss(LIB_ROOT_PATH.concat('com/devexpress/css/dx.common.css'))
    .importCss(LIB_ROOT_PATH.concat('com/devexpress/css/dx.light.css'))
    // 全局配置
    .importCss(LIB_ROOT_PATH.concat('com/lifeonwalden/app/css/app.css'))
    .importScript(LIB_ROOT_PATH.concat('com/lifeonwalden/app/js/config.js'))
    .execute(function() {
        const T = $.jqcToolkit;
        const pinyinParser = new $.jqcPinyin();
        // dxDataGrid数据格式化
        // 时间格式化
        $.timeFormat = function (format) {
            return function (value) {
                return $.jqcDateUtil.format(+value, format);
            }
        }
        // 保留小数
        $.numberFormat = function (num=2, postfix='', prefix='') {
            return function (value) {
                var _temp = typeof value === 'number' ? value : +value;
                return prefix + _temp.toFixed(num) + postfix;
            }
        }
        // 时间戳转时间对象
        $.timestamp2date = function (name) {
            return function (rowData) {
                return rowData[name] ? new Date(+(rowData[name])) : '';
            }
        }
        /* *******************jQuery对象封装********************* */
        $.fn.extend({
            /**
             * params.data  数据
             * params.adapter   适配 value/label
             * params.defaultVal    默认值
             */
            select: function (data, defaultVal) {
                var $el = this;
                var _el = this[0];
                if (!_el || (_el.nodeName != 'INPUT')) {
                    return;
                }
                if ($el.attr('off') != undefined) {
                    return;
                }
                var _data;
                var adapter = {};
                if (data.length) {
                    _data = data;
                    adapter = undefined;
                } else {
                    _data = data.data;
                    adapter = data.adapter || {};
                }
                new $.jqcSelect({
                    el: $el,
                    data: _data,
                    adapter,
                    defaultValue: defaultVal
                });

            },
            selectSearch: function (data, defaultVal) {
                var $el = this;
                var _el = this[0];
                var _data = data;
                var _defaultVal = defaultVal;
                var _updateDataSource = undefined;
                if (!_el || (_el.nodeName != 'INPUT')) {
                    return;
                }
                if ($el.attr('off') != undefined) {
                    return;
                }
                if (this.attr('defaultvalue') != undefined) {
                    _defaultVal = this.attr('defaultvalue');
                }
                if (this.attr('ext') == '*' || _defaultVal == '*') {
                    if ($.isArray(data)) {
                        _data = [{value: '*',label: '全部'}].concat(data);
                    } else {
                        var _value = data.adapter.value;
                        var _filter = data.adapter.filter;
                        var _pinyinFilter = data.adapter.pinyinFilter;
                        var _label = typeof data.adapter.label == 'function' ? data.adapter.pinyinFilter : data.adapter.label;
                        _data.data = [{
                            [_value]: '*',
                            [_label]: '全部',
                            [_filter]: '*',
                            [_pinyinFilter]: '全部',
                        }].concat(_data.data);
                    }
                }
                if (data.refreshApi != undefined) {
                    if (typeof data.refreshApi == 'string') {
                        _updateDataSource = function (callback) {
                            $.ajax(data.refreshApi).then(res => {
                                var result = res.result || [];
                                callback(result);
                            });
                        }
                    } else if (typeof data.refreshApi == 'function') {
                        _updateDataSource = data.refreshApi;
                    }
                }
                _el.jqcSelectBox && _el.jqcSelectBox.destroy();
                var config = {
                    optionData: _data,
                    defaultVal: _defaultVal,
                    dataName: JSON.stringify(_data),
                    autoDisplay: true,
                    supportFuzzyMatch: true,
                    supportPinYin: true,
                    pinyinParser: pinyinParser,
                    withSearch: true,
                    element: $el,
                    updateDataSource: _updateDataSource,
                    onSelect: function (data) {
                        $el.trigger('change', data);
                    }
                };
                _el.jqcSelectBox = new $.jqcSelectBox(config);
            },
            destroySelectBox: function () {
                $.each(this, function (index, el) {
                    if (el.jqcSelectBox) {
                        el.jqcSelectBox.destroy();
                        el.jqcSelectBox = undefined;
                    }
                    if ($(el).data('jqcSelect')) {
                        $(el).data('jqcSelect').destroy();
                    }
                    if ($(el).data('xdsoft_datetimepicker')) {
                        $(el).datetimepicker('destroy');
                    }
                });
            }
        });
        /* ********************************************************** */
        // ajax全局提醒
        $.ajaxSetup({
            complete: function (xhr, status) {
                // 登陆已过期
                if (xhr.responseJSON && xhr.responseJSON.code && xhr.responseJSON.code == 401) {
                    $.jqcConfirm({
                        title: '未登录',
                        content: '您的登录状态已过期,请刷新页面！',
                        onConfirm: function() {
                            window.location.reload();
                        }
                    });
                }
            },
            // 请求失败
            error: function (xhr, status, error) {
                $.jqcNotification({
                    type: 'error',
                    title: '服务器连接失败'
                });
            }
        });
        /**
         * 缓存api请求到的数据
         */
        $.DataPool = function (apisMap) {
            this.map = new Map();
            this.apisMap = apisMap || {};
            this.clear();
        }
        $.DataPool.prototype = {
            set: function (key, value) {
                this.map.set(key, value);
            },
            get: function (key) {
                return this.map.get(key);
            },
            asyncGet: function (key, reload) {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    if (!_this.apisMap[key]) {
                        reject(`storage: 获取${key}的api地址不存在！`);
                        return;
                    }
                    var result;
                    var data = _this.map.has(key);
                    if (data && !reload) {
                        resolve(_this.map.get(key));
                    } else {
                        _this.update(key).then(function (data) {
                            resolve(data);
                        });
                    }
                });
            },
            update: function (key) {
                var _this = this;
                return new Promise((resolve, reject) => {
                    var url = _this.apisMap[key];
                    if (!url) {
                        reject(`storage: ${key}的api地址不存在！`);
                        return;
                    }
                    $.ajax(url).then(res => {
                        if (res.code == 0) {
                            var data = res.result || [];
                            _this.map.set(key, data);
                            resolve(data);
                        } else {
                            reject(res.msg);
                        }
                    })
                });
            },
            clear: function () {
                this.map.clear();
            },
            delete: function (key) {
                this.map.delete(key);
            }
        };
        // 兼容老代码
        $.SessionStorage = $.DataPool;
        /* ********************************************************** */


        var styleCache = {};
        var jqcTab = null;
        $.addForm = function(menu, tab, needReload) {
            var uid = menu.url;
            var text = menu.text;
            if (!jqcTab) {
                jqcTab = tab;
            }
            // tab内存在
            if (tab.index.has(uid)) {
                tab.add({ id: uid });
                if (needReload) {
                    var panel = tab.index.get(uid).panel;
                    var root = panel.find('div[data-tabid]');
                    root.empty();
                    var _app = $.getFormCache(uid);
                    _app.mount(root, menu.data);
                }
                return;
            }
            var _path = APP_ROOT_PATH.concat(menu.url);
            // 第一次点击menu
            if (!$.formCacheHas(uid)) {
                $._globalCacheId = uid;
                $JqcLoader
                    .importScript(_path)
                    .execute(function() {
                        addTabAndCreatePage(_path, text);
                    });
            } else {
                // 关闭后再次点击menu
                addTabAndCreatePage(_path, text);
            }
            function addTabAndCreatePage(path, text) {
                tab.add({
                    id: uid,
                    title: text,
                    content: `<div data-tabid=${uid} data-path=${path} data-name=${text}></div>`,
                    beforeDestroy: function () {
                        var inputs = this.panel.find('input');
                        inputs.destroySelectBox();
                    }
                });
                setTimeout(function() {
                    var panel = tab.index.get(uid).panel;
                    var root = panel.find('div[data-tabid]');
                    $.getFormCache(uid).mount(root, menu.data);
                }, 0);
            }
        };
        $.openPage = function (formUrl, title, data) {
            $.addForm({
                text: title,
                url: formUrl,
                data: data
            }, jqcTab, true);
        }
        // 缓存新加载的js文件数据
        const formCache = new Map();
        // set cache
        $.setupApp = function(App) {
            if (App.mount) {
                if (formCache.has($._globalCacheId)) {
                    return;
                }
                formCache.set($._globalCacheId, App);
            } else {
                throw new Error('$.setupApp: expects a $.App object');
            }
        };
        // get cache
        $.getFormCache = function(uid) {
            return formCache.get(uid);
        };
        // has cache ?
        $.formCacheHas = function(uid) {
            return formCache.has(uid);
        };
        // App页面核心
        $.App = function (params) {
            var _this = this;
            this._root = null; //容器页面根节点
            this._path = ''; //js文件路径
            this.mixinFormat = [];
            this._beforeRender = [];
            this._afterRender = [];
            this.components = [];
            this._dataSource = [];
            if (params && params.mixins && params.mixins.length) {
                params.mixins.forEach(mixin => {
                    for (var key in mixin) {
                        if (key == 'format' && typeof mixin[key] == 'function') {
                            _this.mixinFormat.push(mixin[key].bind(_this));
                        } else if (key == 'afterRender' && typeof mixin[key] == 'function') {
                            _this._afterRender.push(mixin[key].bind(_this));
                        } else if (key == 'beforeRender' && typeof mixin[key] == 'function') {
                            _this._beforeRender.push(mixin[key].bind(_this));
                        } else if (key == 'components' && T.rawType(mixin[key]) == 'Array') {
                            _this.components = _this.components.concat(mixin[key]);
                        } else if (key == 'mixins') {
                            // 忽略
                        } else {
                            _this[key] = mixin[key];
                        }
                    }
                });
            }
            this._config = $.getGlobalConfig(); //config.js文件中的配置
            this.loading = new $.jqcLoading();
            this.pinyinParser = pinyinParser;
            this.components = (params && params.components) ? this.components.concat(params.components) : this.components;  //组件
            this.templatePath = (params && params.templatePath) ? params.templatePath : (this.templatePath || null); //模板文件相对路径
            this.stylePath = (params && params.stylePath) ? params.stylePath : (this.stylePath || null); //模板文件相对路径
            this.contextmenu = (params && params.contextmenu) ? params.contextmenu : (this.contextmenu || null);
            this.dxDataGrid = (params && params.dxDataGrid) ? params.dxDataGrid : (this.dxDataGrid || null);
            if (params && params.beforeRender && typeof params.beforeRender == 'function') {
                this._beforeRender.push(params.beforeRender);
            }
            this._afterRender.push(function (next) {
                this.loading.hide();
                next();
            }.bind(this));
            if (params && params.afterRender && typeof params.afterRender == 'function') {
                this._afterRender.push(params.afterRender);
            }
            _this.queryCallback = params.queryCallback ? params.queryCallback.bind(this) : null;
            this.root = null; //暴露给afterRender的容器根节点
            Object.defineProperty(this, 'dataSource', {
                get: function () {
                    return _this._dataSource;
                },
                set: function (val) {
                    _this._dataSource = [].concat(val);
                    $.jqcEvent.emit('dataSourceLoaded.app', val, _this);
                }
            });
            $.setupApp(this);
            $.jqcEvent.emit('setup.app', this);
        };
        $.App.prototype.mount = function (root, data) {
            var _this = this;
            this._root = root;
            this.root = root;
            this._path = root.attr('data-path');
            this._name = root.attr('data-name');
            this._id = root.attr('data-tabid');
            this.loading.show('加载中...');
            // 生命周期-装载之前
            var _beforeRender = [].concat(this._beforeRender);
            _beforeRender.push(function () {
                $JqcLoader.importComponents(COMP_LIB_PATH, _this.components).execute(function () {
                    if (_this.stylePath) {
                        var _path = _this.getAbsolutePath(_this.stylePath);
                        if (!styleCache[_path]) {
                            $JqcLoader.importCss(_path).execute(function () {
                                styleCache[_path] = true;
                            });
                        }
                    }
                    if (_this.contextmenu) {
                        _this.__renderContextMenu();
                    }
                    if (_this.templatePath) {
                        _this.__getTemplateAndRender(data);
                    } else {
                        if (_this.dxDataGrid) {
                            _this.__renderDxDataGrid();
                        }
                        // 生命周期-渲染之后
                        _this.__afterRender(data);
                    }
                });
            });
            $.jqcEvent.emit('beforeRender.app', this);
            queue(_beforeRender, this);
        };
        $.App.prototype.requestGet = function (api, params, loadingText) {
            var _this = this;
            if (typeof loadingText == 'string') {
                this.loading.show(loadingText)
            } else if (typeof loadingText == 'boolean' && loadingText) {
                this.loading.show();
            }
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
                url: api,
                data: _params
            }).done(res => {
                _this.loading.hide();
            });
        };
        $.App.prototype.requestPost = function (api, params, loadingText) {
            var _this = this;
            if (typeof loadingText == 'string') {
                this.loading.show(loadingText)
            } else if (typeof loadingText == 'boolean' && loadingText) {
                this.loading.show();
            }
            return $.ajax({
                url: api,
                method: 'POST',
                data: params
            }).done(res => {
                _this.loading.hide();
            });
        }
        $.App.prototype.getFile = function (relativePath) {
            var _this = this;
            return $.ajax(_this.getAbsolutePath(relativePath) + '?v=' + +new Date());
        };
        $.App.prototype.getAbsolutePath = function (relativePath) {
            var _this = this;
            var _pathArr = this._path.split('/')
            _pathArr.pop();
            var _relativePath = relativePath.split('/');
            _relativePath.forEach(function(item) {
                if (item == '.') {
                    return;
                } else if (item == '..') {
                    _pathArr.pop();
                } else {
                    _pathArr.push(item);
                }
            });
            return _pathArr.join('/');
        };
        $.App.prototype.__getTemplateAndRender = function (data) {
            var _this = this;
            this.getFile(_this.templatePath).then(res => {
                var _fakeBox = $('<div>').append($(res));
                _this._conditionHtml = _fakeBox.find(`.${_this._config.templateClassNameMap.conditionHtmlClassName}`);
                _this._controlHtml = _fakeBox.find(`.${_this._config.templateClassNameMap.controlHtmlClassName}`);
                _this._contentHtml = _fakeBox.find(`.${_this._config.templateClassNameMap.contentHtmlClassName}`);
                // toolbar
                if (_this._conditionHtml.length || _this._controlHtml.length) {
                    _this.__renderToolBar();
                }
                // content
                if (_this._contentHtml) {
                    _this._root.append(_this._contentHtml);
                }
                // dx表格
                if (_this.dxDataGrid) {
                    _this.__renderDxDataGrid();
                }
                _this.__afterRender(data);
            });
        };
        $.App.prototype.__renderToolBar = function () {
            var _this = this;
            this._toolBar = $('<div>');
            this._root.prepend(_this._toolBar);
            this.toolbar = new $.jqcFormToolBar({
                element: _this._toolBar,
                conditionHtml: _this._conditionHtml[0] || '',
                controlHtml: _this._controlHtml[0] || '',
                height: 50,
                onResize: _this.root.parents('.jqcDialogContainer').length == 0 ? function (height) {
                    if (_this.dxDataGrid && _this.getDxDataGrid()) {
                        _this.getDxDataGrid().option('height', window.innerHeight - 110 - height);
                    }
                } : null,
                onChange: function (height) {
                    if (_this.dxDataGrid && _this.getDxDataGrid()) {
                        _this.getDxDataGrid().option('height', window.innerHeight - 110 - height);
                    }
                }
            });
            setTimeout(function () {
                _this.mixinFormat.forEach(format => {
                    format(_this._toolBar);
                });
                $.formUtil.format(_this._toolBar);
                _this._toolBar.find('.toolbar-left button.queryBtn').click(function () {
                    var _data = $.formUtil.fetch(_this._toolBar);
                    _this.queryCallback && _this.queryCallback(_data);
                    if (_this.dxDataGrid) {
                        _this.fillDxDataGrid(_data);
                        return;
                    }
                });
            }, 0);
            var _parent = this.root.parents('.jqcTabPanel');
            $.jqcEvent.on('resize.menu', function () {
                if (_parent.is(':visible')) {
                    _this.toolbar.resize();
                }
            })
        };
        $.App.prototype.__renderDxDataGrid = function () {
            var _this = this;
            this._dxDataGrid = $('<div data-dx="a">');
            this._root.append(_this._dxDataGrid);
            var _columns = this.dxDataGrid.hideOrder ? _this.dxDataGrid.columns : [].concat(_this._config.dxDataGridDefaultConfig.columns, _this.dxDataGrid.columns);
            var columnsAddDataType = _columns.map(item => {
                var _item = {};
                var _temp = {};
                var dataField = item.dataField;
                if (!item.hasOwnProperty('dataType')) {
                    item.dataType = 'string';
                }
                if (item.dataType === 'date') {     // 日期
                    _temp = {
                        format: $.timeFormat(),
                        width: 150,
                        calculateCellValue: function (rowData) {
                            return rowData[dataField] ? new Date(+(rowData[dataField])) : '';
                        }
                    }
                } else if (item.dataType === 'datetime') {      // 时间
                    _temp = {
                        format: $.timeFormat('yyyy-MM-dd HH:mm:ss'),
                        width: 180,
                        calculateCellValue: function (rowData) {
                            return rowData[dataField] ? new Date(+(rowData[dataField])) : '';
                        }
                    }
                } else if (item.dataType === 'boolean') {       // 布尔   0：否， 1：是
                    item.dataType = 'string';
                    _temp = {
                        calculateCellValue: function (rowData) {
                            if (rowData[dataField] === '' || rowData[dataField] === undefined) {
                                return '';
                            }
                            return rowData[dataField] ? '是' : '否';
                        }
                    }
                }
                Object.assign(_item, _temp, item);
                return _item;
            });
            var dxConfig = $.extend({}, _this._config.dxDataGridDefaultConfig, _this.dxDataGrid, {columns: columnsAddDataType});
            if (this.dxDataGrid.hideOrder) {
                dxConfig.scrolling = {
                    mode: 'virtual'
                }
            }
            if (this._contextmenu) {
                dxConfig.onContextMenuPreparing = function(e) {
                    e.jQueryEvent.preventDefault();
                    if (!e.row || e.row.rowType !== 'data') {
                        return;
                    }
                    _this._contextmenu.show(e.row.data);
                }
            }
            if (!dxConfig.rowAlternationEnabled) {
                dxConfig.elementAttr = {
                    class: 'custom-row-style'
                }
            }
            /**
             * dxDataGrid表格 行
             * 1.添加单击事件 onRowClick
             * 2.添加双击事件 onRowDblClick
             */
            var _onRowClick = dxConfig.onRowClick;
            var _onRowDblClick = dxConfig.onRowDblClick;
            var clickCount = 0;
            var timer = null;
            dxConfig.onRowClick = function (e) {
                clickCount ++;
                clearTimeout(timer);
                timer = setTimeout(() => {
                   if (clickCount < 2) {
                       _onRowClick && _onRowClick(e);
                   } else {
                       _onRowDblClick && _onRowDblClick(e);
                   }
                   clickCount = 0;
                }, 300);
            }
            this._dxDataGrid.dxDataGrid(dxConfig);
            var dx = this.getDxDataGrid();
            // 导出事件绑定
            if (dx && dxConfig.exportProxyClassName) {
                this.root.find(`.${dxConfig.exportProxyClassName}`)
                    .click(function(e) {
                        e.stopPropagation();
                        dx.exportToExcel(false);
                    });
            }
            // 搜索事件绑定
            if (dx && dxConfig.searchProxyClassName) {
                this.root.find(`.${dxConfig.searchProxyClassName}`)
                    .keyup(function(e) {
                        e.stopPropagation();
                        var val = $(this).val();
                        dx.searchByText(val);
                    });
            }
        };
        $.App.prototype.fillDxDataGridByData = function (data) {
            var _this = this;
            this.dataSource = data || [];
            this._dxDataGrid && this.getDxDataGrid().option('dataSource', data);
        };
        $.App.prototype.getDxDataGrid = function () {
            return this._dxDataGrid ? this._dxDataGrid.dxDataGrid('instance') : undefined;
        };
        $.App.prototype.fillDxDataGrid = function (params) {
            var _this = this;
            this.requestGet(_this.dxDataGrid.fetchDataApi, params, '加载中...').then(res => {
                _this.fillDxDataGridByData(res.result || []);
            });
        };
        $.App.prototype.__renderContextMenu = function () {
            var _this = this;
            var _options = $.extend({}, {
                width: 118,
                height: 40
            }, this.contextmenu);
            this._contextmenu = new $.jqcContextMenu(_options);
            setTimeout(function () {
                $(document).on('mousewheel.$App', function () {
                    _this._contextmenu.box && _this._contextmenu.box.remove();
                });
            }, 0);
        };
        $.App.prototype.__afterRender = function (data) {
            var _this = this;
            var _afterRender = [].concat(this._afterRender);
            // 带参数渲染
            _afterRender.splice(-1, 0, function (next) {
                if (data) {
                    var search = this.root.find('.toolbar-left');
                    if (search.length) {
                        $.formUtil.fill(search, data)
                    }
                }
                next();
            });
            setTimeout(function () {
                queue(_afterRender, _this);
                _this.loading.hide();
            }, 17);
        };
        /**
         * templatePath 模板路径
         * title dialog title
         * dafaultData 默认填充数据
         * readOnly  Boolean 只读，全部设置disabled
         * dataMerge Boolean 数据合并 绑定数据与defaultData合并,有则覆盖。用于数据更新、关联。
         * api 更新 添加的api
         * disabled Array databind属性的值 ['*'] 等同于 readOnly = true;
         */
        $.App.prototype.dialog = function (params) {
            var _this = this;
            var _dialog;
            // 没有模板
            if (!params.templatePath) {
                return;
            }
            this.getFile(params.templatePath).then(res => {
                var _template = $(res);
                var $btn = _template.find('button.done');
                var $next = _template.find('button.save_and_add');
                _dialog = new $.jqcDialog({
                    title: params.title || '',
                    content: _template,
                    width: params.width || 1080,
                    afterClose: function () {
                        params.afterClose && params.afterClose();
                        $.each(_template.find('input'), function (index, el) {
                            if (el.jqcSelectBox) {
                                el.jqcSelectBox.destroy();
                            }
                            if ($(el).data('xdsoft_datetimepicker')) {
                                $(el).datetimepicker('destroy');
                            }
                        })
                    }
                });
                if (Array.isArray(params.disabled)) {
                    if (params.disabled.length === 1 && params.disabled[0] === '*') {
                        _template.find('[databind]').attr('disabled', 'disabled');                        
                    } else {
                        params.disabled.forEach(item => {
                            _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                        });
                    }
                }
                _this.mixinFormat.forEach(format => {
                    format(_template);
                });
                setTimeout(function () {
                    _dialog.content.scrollTop(0);
                    params.afterRender && params.afterRender(_template, _dialog);
                    if (params.defaultData) {
                        $.formUtil.fill(_template, params.defaultData);
                    } else {
                        $.formUtil.format(_template);
                    }
                }, 10);
                if (params.readOnly) {
                    _template.find('[databind]').attr('disabled', 'disabled');
                    _template.find('button').hide();
                }
                _dialog.open();
                if ($next.length == 1) {
                    $next.click(function () {
                        _this.loading.lock(500);
                        $btn.length == 1 && $btn.trigger('click', 'next');
                    })
                }
                $btn.click(function (e, type) {
                    var loadingTxt = $(this).attr('loading');
                    if (loadingTxt == undefined) {
                        loadingTxt = false
                    } else {
                        loadingTxt = loadingTxt || true;
                    }
                    setTimeout(function () {
                        // 提交操作队列
                        var submit_queue = [];
                        // 获取数据之前执行
                        params.beforeFetchData && submit_queue.push(function (next) {
                            params.beforeFetchData(_template, next, _dialog, type);
                        });
                        // 获取数据
                        submit_queue.push(function fetchData(next) {
                            var _data = $.formUtil.fetch(_template);
                            if (!params.isInsert) {
                                _data = Object.assign({}, params.defaultData, _data);
                            }
                            if (params.check && !params.check(_data)) {
                                return;
                            }
                            next(_data);
                        });
                        // 提交数据之前执行
                        params.beforeSubmit && submit_queue.push(function (next, _data) {
                            params.beforeSubmit(_data, _template, next, _dialog, type);
                        });
                        // 提交数据
                        submit_queue.push(function (next, _data) {
                            submit(_data, next)
                        });
                        // 提交数据之后执行
                        params.afterSubmit && submit_queue.push(function (next, res, success, failded) {
                            params.afterSubmit(res, success, failded, _template, _dialog, type);
                        });
                        queue(submit_queue);
                        function submit(_data, next) {
                            _this.requestPost(params.api, _data, loadingTxt).then(res => {
                                // 异步回调
                                function success() {
                                    if (params.updateCache && _this.updateCache) {
                                        _this.updateCache(params.updateCache);
                                    }
                                    $.jqcNotification({
                                        type: 'success',
                                        title: '操作成功'
                                    });
                                    _this.triggerQuery(params.fillParams);
                                    // 新增下一个
                                    if (type == 'next') {
                                        _template.find('input').val('');
                                        $.formUtil.fill(_template, params.defaultData);
                                        if (Array.isArray(params.disabled)) {
                                            if (params.disabled.length === 1 && params.disabled[0] === '*') {
                                                _template.find('[databind]').attr('disabled', 'disabled');                        
                                            } else {
                                                params.disabled.forEach(item => {
                                                    _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                                                });
                                            }
                                        }
                                    } else {
                                        _dialog.close();
                                    }
                                }
                                function failed() {
                                    $.jqcNotification({
                                        type: 'error',
                                        title: '操作失败。',
                                        content: res.msg
                                    });
                                }
                                if (params.afterSubmit) {
                                    next(res, success, failed);
                                    return;
                                }
                                // 默认同步代码
                                if (res.code == 0) {
                                    if (type == 'next') {
                                        _template.find('input').val('');
                                        $.formUtil.fill(_template, params.defaultData);
                                        if (Array.isArray(params.disabled)) {
                                            if (params.disabled.length === 1 && params.disabled[0] === '*') {
                                                _template.find('[databind]').attr('disabled', 'disabled');                        
                                            } else {
                                                params.disabled.forEach(item => {
                                                    _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                                                });
                                            }
                                        }
                                    } else {
                                        _dialog.close();
                                    }
                                    _this.triggerQuery(params.fillParams);
                                    if (params.success) {
                                        params.success(res, _dialog);
                                    } else {
                                        var config = {
                                            type: 'success',
                                            title: '操作成功'
                                        };
                                        if (res.msg != undefined) {
                                            config.content = res.msg;
                                        }
                                        $.jqcNotification(config);
                                    }
                                    if (params.updateCache && _this.updateCache) {
                                        _this.updateCache(params.updateCache);
                                    }
                                } else {
                                    if (params.failed) {
                                        params.failed(res, _dialog);
                                    } else {
                                        $.jqcNotification({
                                            type: 'error',
                                            title: '操作失败。',
                                            content: res.msg
                                        });
                                    }
                                }
                            });
                        }
                    }, 20);
                })
            });
        };
        $.App.prototype.triggerQuery = function (params) {
            var queryBtn = this.root.find('.toolbar-left button.queryBtn');
            if (queryBtn.length) {
                queryBtn.trigger('click');
            } else if (this.dxDataGrid){
                this.fillDxDataGrid(params);
            } else {
                // nothing
            }
        };
        $.App.prototype.delete = function (params) {
            var _this = this;
            $.jqcConfirm({
                title: params.title,
                content: params.content,
                onConfirm: function () {
                    if (!params.api || !params.data) {
                        throw new Error('delete方法缺少“api”或“data”属性！');
                    }
                    _this.requestPost(params.api, params.data, '删除中...').then(res => {
                        if (res.code == 0) {
                            _this.triggerQuery(params.fillParams);
                            if (params.success) {
                                params.success(res);
                            } else {
                                var config = {
                                    type: 'success',
                                    title: '删除成功'
                                };
                                if (res.msg != undefined) {
                                    config.content = res.msg;
                                }
                                $.jqcNotification(config);
                            }
                            if (params.updateCache && _this.updateCache) {
                                _this.updateCache(params.updateCache);
                            }
                        } else {
                            $.jqcNotification({
                                type: 'error',
                                title: '删除失败',
                                content: res.msg
                            });
                        }
                    });    
                }
            });
        };
        $.App.prototype.createSelectBox = function (params) {
            var _this = this;
            params.element[0].jqcSelectBox && params.element[0].jqcSelectBox.destroy();
            var config = {
                dataName: '',
                supportFuzzyMatch: true,
                supportPinYin: true,
                pinyinParser: _this.pinyinParser,
                width: 200,
                onSelect: function (data) {
                    params.element && params.element.trigger('change', data);
                }
            };
            Object.assign(config, params);
            params.element[0].jqcSelectBox = new $.jqcSelectBox(config);
        };
        // 打开子页面
        $.App.prototype.openChildPage = function ({app, title, width, height}) {
            var _this = this;
            title = title || '';
            width = width || 1080;
            height = height || 470;
            var $root = $('<div>').attr('data-path', _this._path).addClass('jqcTabPanel').css('height', height + 80);
            var _dialog = new $.jqcDialog({
                title: title,
                content: $root,
                width: width
            });
            _dialog.open();
            if (!app || !app.mount) {
                throw new Error('openChildPage expect a $.App instance!');
            }
            app.mount($root);
        }
        function queue (funcs, scope) {
            (function next() {
                if(funcs.length > 0) {
                    funcs.shift().apply(scope || {}, [next].concat(Array.prototype.slice.call(arguments, 0)));
                }
            })();
        };
        // 单独dialog页面
        if (window.__PAGE_TYPE__ == 'dialog') {
            $.App.prototype.dialog = function (params) {
                var _this = this;
                var _dialog;
                // 没有模板
                if (!params.templatePath) {
                    return;
                }
                this.getFile(params.templatePath).then(res => {
                    var _template = $(res);
                    _dialog = $('<div>').css({
                        width: params.width || 1080
                    });
                    $('.dialog_title').text(params.title);
                    $('.dialog_body').append(_dialog);
                    _dialog.append(_template);
                    var $btn = _template.find('button.done');
                    var $next = _template.find('button.save_and_add');
                    if (Array.isArray(params.disabled)) {
                        if (params.disabled.length === 1 && params.disabled[0] === '*') {
                            _template.find('[databind]').attr('disabled', 'disabled');                        
                        } else {
                            params.disabled.forEach(item => {
                                _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                            });
                        }
                    }
                    _this.mixinFormat.forEach(format => {
                        format(_template);
                    });
                    setTimeout(function () {
                        params.afterRender && params.afterRender(_template, _dialog);
                        if (params.defaultData) {
                            $.formUtil.fill(_template, params.defaultData);
                        } else {
                            $.formUtil.format(_template);
                        }
                    }, 10);
                    if (params.readOnly) {
                        _template.find('[databind]').attr('disabled', 'disabled');
                        _template.find('button').hide();
                    }
                    if ($next.length == 1) {
                        $next.click(function () {
                            _this.loading.lock(500);
                            $btn.length == 1 && $btn.trigger('click', 'next');
                        })
                    }
                    $btn.click(function (e, type) {
                        var loadingTxt = $(this).attr('loading');
                        if (loadingTxt == undefined) {
                            loadingTxt = false
                        } else {
                            loadingTxt = loadingTxt || true;
                        }
                        setTimeout(function () {
                            // 提交操作队列
                            var submit_queue = [];
                            // 获取数据之前执行
                            params.beforeFetchData && submit_queue.push(function (next) {
                                params.beforeFetchData(_template, next);
                            });
                            // 获取数据
                            submit_queue.push(function fetchData(next) {
                                var _data = $.formUtil.fetch(_template);
                                if (!params.isInsert) {
                                    _data = Object.assign({}, params.defaultData, _data);
                                }
                                if (params.check && !params.check(_data)) {
                                    return;
                                }
                                next(_data);
                            });
                            // 提交数据之前执行
                            params.beforeSubmit && submit_queue.push(function (next, _data) {
                                params.beforeSubmit(_data, _template, next);
                            });
                            // 提交数据
                            submit_queue.push(function (next, _data) {
                                submit(_data, next)
                            });
                            // 提交数据之后执行
                            params.afterSubmit && submit_queue.push(function (next, res, success, failded) {
                                params.afterSubmit(res, success, failded);
                            });
                            queue(submit_queue);
                            function submit(_data, next) {
                                _this.requestPost(params.api, _data, loadingTxt).then(res => {
                                    // 异步回调
                                    function success() {
                                        if (params.updateCache && _this.updateCache) {
                                            _this.updateCache(params.updateCache);
                                        }
                                        $.jqcNotification({
                                            type: 'success',
                                            title: '操作成功'
                                        });
                                        _this.triggerQuery(params.fillParams);
                                        // 新增下一个
                                        if (type == 'next') {
                                            _template.find('input').val('');
                                            $.formUtil.fill(_template, params.defaultData);
                                            if (Array.isArray(params.disabled)) {
                                                if (params.disabled.length === 1 && params.disabled[0] === '*') {
                                                    _template.find('[databind]').attr('disabled', 'disabled');                        
                                                } else {
                                                    params.disabled.forEach(item => {
                                                        _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                                                    });
                                                }
                                            }
                                        } else {
                                            window.close();
                                        }
                                    }
                                    function failed() {
                                        $.jqcNotification({
                                            type: 'error',
                                            title: '操作失败。',
                                            content: res.msg
                                        });
                                    }
                                    if (params.afterSubmit) {
                                        next(res, success, failed);
                                        return;
                                    }
                                    // 默认同步代码
                                    if (res.code == 0) {
                                        if (type == 'next') {
                                            _template.find('input').val('');
                                            $.formUtil.fill(_template, params.defaultData);
                                            if (Array.isArray(params.disabled)) {
                                                if (params.disabled.length === 1 && params.disabled[0] === '*') {
                                                    _template.find('[databind]').attr('disabled', 'disabled');                        
                                                } else {
                                                    params.disabled.forEach(item => {
                                                        _template.find(`[databind=${item}]`).attr('disabled', 'disabled');
                                                    });
                                                }
                                            }
                                        } else {
                                            window.close();
                                        }
                                        _this.triggerQuery(params.fillParams);
                                        if (params.success) {
                                            params.success(res, _dialog);
                                        } else {
                                            var config = {
                                                type: 'success',
                                                title: '操作成功'
                                            };
                                            if (res.msg != undefined) {
                                                config.content = res.msg;
                                            }
                                            $.jqcNotification(config);
                                        }
                                        if (params.updateCache && _this.updateCache) {
                                            _this.updateCache(params.updateCache);
                                        }
                                    } else {
                                        if (params.failed) {
                                            params.failed(res, _dialog);
                                        } else {
                                            $.jqcNotification({
                                                type: 'error',
                                                title: '操作失败。',
                                                content: res.msg
                                            });
                                        }
                                    }
                                });
                            }
                        }, 20);
                    })
                });
            };
        }
    });