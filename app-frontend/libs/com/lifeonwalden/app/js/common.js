$JqcLoader.registerModule($JqcLoader.newModule('com.jquery', LIB_ROOT_PATH).registerComponents(['jquery', 'keycode', 'version']))
    .registerModule($JqcLoader.newModule('com.lifeonwalden.jqc', LIB_ROOT_PATH)
        .registerComponents(['baseElement', 'blocker', 'cache', 'dateUtil', 'dialog', 'draggable', 'format', 'inputNumber', 'lang'])
        .registerComponents(['loader', 'location', 'pinyin', 'resizeable', 'selectbox', 'slide', 'uniqueKey', 'slide', 'uniqueKey'])
        .registerComponents(['valHooks', 'zindex'])
        .registerComponents(['toolkit'])
        .registerComponents(['menuTree'])
        .registerComponents(['notification'])
        .registerComponents(['contextmenu','layoutHelper'])
        .registerComponents(['loading'])
        .registerComponents(['confirm'])
        .registerComponents(['event'])
        .registerComponents(['formToolBar', 'formUtil', 'datetimepicker', 'tip', 'msg', 'tab'])
        .registerComponents(['echarts']) //图表
        .registerComponents(['jsoneditor']) //json编辑器图表
        .registerComponents(['editor']) //富文本编辑器
        .registerComponents(['apisBox']));

const COMP_LIB_PATH = 'com.lifeonwalden.jqc';

$JqcLoader.importComponents('com.jquery', ['jquery', 'keycode', 'version'])
    .importComponents('com.lifeonwalden.jqc', ['confirm', 'event', 'menuTree', 'formUtil', 'msg', 'tab', 'dialog', 'formToolBar', 'contextmenu', 'toolkit', 'loading','layoutHelper', 'notification'])
    // dx组件
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/jszip.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.web.debug.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.messages.cn.js'))
    // echarts
    .importCss(LIB_ROOT_PATH.concat('com/devexpress/css/dx.common.css'))
    .importCss(LIB_ROOT_PATH.concat('com/devexpress/css/dx.light.css'))
    // datetimepicker样式
    .importCss(LIB_ROOT_PATH.concat('com/lifeonwalden/jqc/datetimepicker/css/datetimepicker.css'))
    // jsoneditor样式
    .importCss(LIB_ROOT_PATH.concat('com/lifeonwalden/jqc/jsoneditor/css/jsoneditor.css'))
    // 全局配置
    .importCss(LIB_ROOT_PATH.concat('com/lifeonwalden/app/css/app.css'))
    .importCss(LIB_ROOT_PATH.concat('com/lifeonwalden/app/css/grid.css'))
    .importScript(LIB_ROOT_PATH.concat('com/lifeonwalden/app/js/config.js'))
    .execute(function() {
        const T = $.jqcToolkit;
        const pinyinParser = new $.jqcPinyin();
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
                _el.jqcSelectBox && _el.jqcSelectBox.destroy();
                var config = {
                    optionData: data,
                    defaultVal: defaultVal,
                    dataName: JSON.stringify(data),
                    withSearch: false,
                    autoDisplay: true,
                    element: $el,
                    onSelect: function (data) {
                        $el.trigger('change', data);
                    }
                };
                _el.jqcSelectBox = new $.jqcSelectBox(config);

            },
            selectSearch: function (data, defaultVal) {
                var $el = this;
                var _el = this[0];
                if (!_el || (_el.nodeName != 'INPUT')) {
                    return;
                }
                _el.jqcSelectBox && _el.jqcSelectBox.destroy();
                var config = {
                    optionData: data,
                    defaultVal: defaultVal,
                    dataName: JSON.stringify(data),
                    autoDisplay: true,
                    supportFuzzyMatch: true,
                    supportPinYin: true,
                    pinyinParser: pinyinParser,
                    element: $el,
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
                    }
                    if ($(el).data('xdsoft_datetimepicker')) {
                        $(el).datetimepicker('destroy');
                    }
                });
            }
        });
        /* ********************************************************** */

        /* **********************sessionStorage************************ */
        $.SessionStorage = function (apisMap) {
            this.apisMap = apisMap;
        }
        $.SessionStorage.prototype = {
            set: function (key, value) {
                var _value;
                try {
                    _value = JSON.stringify(value);
                } catch (error) {
                    _value = value;
                }
                window.sessionStorage.setItem(key, _value);
            },
            get: function (key) {
                var result;
                var data = window.sessionStorage.getItem(key);
                try {
                    result = JSON.parse(data);
                } catch (error) {
                    result = data;
                }
                return(result);
            },
            asyncGet: function (key, reload) {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    if (!_this.apisMap[key]) {
                        reject(`storage: 获取${key}的api地址不存在！`);
                        return;
                    }
                    var result;
                    var data = window.sessionStorage.getItem(key);
                    if (data && !reload) {
                        try {
                            result = JSON.parse(data);
                        } catch (error) {
                            result = data;
                        }
                        resolve(result);
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
                            _this.set(key, data);
                            resolve(data);
                        } else {
                            reject(res.msg);
                        }
                    })
                });
            },
            clear: function () {
                window.sessionStorage.clear();
            },
            remove: function (key) {
                window.sessionStorage.removeItem(key);
            }
        };
        /* ********************************************************** */


        var styleCache = {};
        $.addForm = function(menu, tab) {
            var uid = menu.id;
            var text = menu.text;
            // tab内存在
            if (tab.index.has(uid)) {
                tab.add({ id: uid });
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
                    content: `<div data-tabid=${uid} data-path=${path} data-name=${text}></div>`
                });
                setTimeout(function() {
                    var panel = tab.index.get(uid).panel;
                    var root = panel.find('div[data-tabid=' + uid + ']');
                    $.getFormCache(uid).mount(root, panel);
                }, 0);
            }
        };
        // 缓存新加载的js文件数据
        const formCache = new Map();
        // set cache
        $.setupApp = function(App) {
            if (App.mount) {
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
            if (params && params.afterRender && typeof params.afterRender == 'function') {
                this._afterRender.push(params.afterRender);
            }
            this.root = null; //暴露给afterRender的容器根节点
            $.setupApp(this);
        };
        $.App.prototype.mount = function (root, panel) {
            var _this = this;
            this._root = root;
            this.root = root;
            if (panel) {
                panel.data('destroy', function () {
                    var inputs = _this.root.find('input');
                    inputs.destroySelectBox();
                });
            }
            this._path = root.attr('data-path');
            this._name = root.attr('data-name');
            this.loading.show();
            // 生命周期-装载之前
            var _beforeRender = [].concat(this._beforeRender);
            _beforeRender.push(function () {
                $JqcLoader.importComponents(COMP_LIB_PATH, this.components).execute(function () {
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
                        _this.__getTemplateAndRender();
                    } else {
                        if (_this.dxDataGrid) {
                            _this.__renderDxDataGrid();
                        }
                        // 生命周期-渲染之后
                        _this.__afterRender();
                    }
                });
            });
            queue(_beforeRender, this);
        };
        $.App.prototype.requestGet = function (api, params) {
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
                url: api,
                data: _params
            });
        };
        $.App.prototype.requestPost = function (api, params) {
            var _this = this;
            return $.ajax({
                url: api,
                method: 'POST',
                data: params
            });
        }
        $.App.prototype.getFile = function (relativePath) {
            var _this = this;
            return $.ajax(_this.getAbsolutePath(relativePath));
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
        $.App.prototype.__getTemplateAndRender = function () {
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
                _this.__afterRender();
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
                    if (_this.dxDataGrid) {
                        _this.getDxDataGrid().option('height', window.innerHeight - 110 - height);
                    }
                } : null
            });
            setTimeout(function () {
                _this.mixinFormat.forEach(format => {
                    format(_this._toolBar);
                });
                $.formUtil.format(_this._toolBar);
                if (!_this.dxDataGrid) {
                    return;
                }
                _this._toolBar.find('.toolbar-left button.queryBtn').click(function () {
                    var _data = $.formUtil.fetch(_this._toolBar);
                    _this.fillDxDataGrid(_data);
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
            var _columns = [].concat(_this._config.dxDataGridDefaultConfig.columns, _this.dxDataGrid.columns);
            var dxConfig = $.extend({}, _this._config.dxDataGridDefaultConfig, _this.dxDataGrid, {columns: _columns});
            if (this._contextmenu) {
                dxConfig.onContextMenuPreparing = function(e) {
                    e.jQueryEvent.preventDefault();
                    if (!e.row || e.row.rowType !== 'data') {
                        return;
                    }
                    _this._contextmenu.show(e.row.data);
                }
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
            this._dxDataGrid && this.getDxDataGrid().option('dataSource', data);
        };
        $.App.prototype.getDxDataGrid = function () {
            return this._dxDataGrid ? this._dxDataGrid.dxDataGrid('instance') : undefined;
        };
        $.App.prototype.fillDxDataGrid = function (params) {
            var _this = this;
            this.loading.show();
            this.requestGet(_this.dxDataGrid.fetchDataApi, params).then(res => {
                _this.fillDxDataGridByData(res.result || []);
                _this.loading.hide();
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
        $.App.prototype.__afterRender = function () {
            var _this = this;
            var _afterRender = [].concat(this._afterRender);
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
                // storage.format(_template);
                setTimeout(function () {
                    params.afterRender && params.afterRender(_template, _dialog);
                    params.defaultData && $.formUtil.fill(_template, params.defaultData);
                }, 10);
                if (params.readOnly) {
                    _template.find('[databind]').attr('disabled', 'disabled');
                    _template.find('button').hide();
                }
                _dialog.open();
                $btn.click(function () {
                    var _data = $.formUtil.fetch(_template);
                    if (!params.isInsert) {
                        _data = Object.assign({}, params.defaultData, _data);
                    }
                    _this.requestPost(params.api, _data).then(res => {
                        if (res.code == 0) {
                            _dialog.close();
                            _this.triggerQuery(params.fillParams);
                            if (params.success) {
                                params.success(res);
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
                            $.jqcNotification({
                                type: 'error',
                                title: '操作失败。',
                                content: res.msg
                            });
                        }
                    });
                })
            });
        };
        $.App.prototype.triggerQuery = function (params) {
            if (this._toolBar) {
                this._toolBar.find('.toolbar-left button.queryBtn').trigger('click');
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
                    _this.requestPost(params.api, params.data).then(res => {
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
        function queue (funcs, scope) {
            (function next() {
                if(funcs.length > 0) {
                    funcs.shift().apply(scope || {}, [next].concat(Array.prototype.slice.call(arguments, 0)));
                }
            })();
        };
    });