$JqcLoader.registerModule($JqcLoader.newModule('com.jquery', LIB_ROOT_PATH).registerComponents(['jquery', 'keycode', 'version']))
    .registerModule($JqcLoader.newModule('com.lifeonwalden.jqc', LIB_ROOT_PATH)
        .registerComponents(['baseElement', 'blocker', 'cache', 'dateUtil', 'dialog', 'draggable', 'format', 'inputNumber', 'lang'])
        .registerComponents(['loader', 'location', 'pinyin', 'resizeable', 'selectbox', 'slide', 'uniqueKey', 'slide', 'uniqueKey'])
        .registerComponents(['valHooks', 'zindex'])
        .registerComponents(['toolkit'])
        .registerComponents(['menu'])
        .registerComponents(['notification'])
        .registerComponents(['contextmenu','layoutHelper'])
        .registerComponents(['loading'])
        .registerComponents(['formToolBar', 'formUtil', 'datetimepicker', 'tip', 'msg', 'tab', 'jsoneditor'])
        .registerComponents(['apisBox']));

$JqcLoader.importComponents('com.jquery', ['jquery', 'keycode', 'version'])
    .importComponents('com.lifeonwalden.jqc', ['menu', 'formUtil', 'msg', 'tab', 'formToolBar', 'contextmenu', 'toolkit', 'loading','layoutHelper', 'notification', 'jsoneditor'])
    // dx组件
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/jszip.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.web.debug.js'))
    .importScript(LIB_ROOT_PATH.concat('com/devexpress/dx.messages.cn.js'))
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
        $.datetimepicker.setLocale('zh');
        const YEAR = new Date().getFullYear();
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
                $.ajax(_path).then(res => {
                    $._globalCacheId = uid;
                    $JqcLoader
                        .importScript(_path)
                        .execute(function() {
                            addTabAndCreatePage(_path, text);
                        });
                }).catch(err => {
                    console.error(err);
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
                    $.getFormCache(uid).mount($('div[data-tabid=' + uid + ']'));
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
            this._root = null; //容器页面根节点
            this._path = ''; //js文件路径
            this._config = $.getGlobalConfig(); //config.js文件中的配置
            this.loading = new $.jqcLoading();
            this.pinyinParser = new $.jqcPinyin();
            this.templatePath = params.templatePath ? params.templatePath : null; //模板文件相对路径
            this.stylePath = params.stylePath ? params.stylePath : null; //模板文件相对路径
            this.contextmenu = (params && params.contextmenu) ? params.contextmenu : null;
            this.dxDataGrid = (params && params.dxDataGrid) ? params.dxDataGrid : null;
            this.afterRender = (params && params.afterRender) ? params.afterRender.bind(this) : null;
            this.root = null; //暴露给afterRender的容器根节点
            this.styleLoaded = false;
        };
        $.App.prototype.mount = function (root) {
            var _this = this;
            this._root = root;
            this.root = root;
            this._path = root.attr('data-path');
            this._name = root.attr('data-name');
            this.loading.show();
            // 生命周期-装载之前
            this.beforeMount && this.beforeMount();
            if (this.stylePath && !this.styleLoaded) {
                var _path = this.getAbsolutePath(_this.stylePath);
                $JqcLoader.importCss(_path).execute(function () {
                    _this.styleLoaded = true;
                });
            }
            if (this.contextmenu) {
                this.__renderContextMenu();
            }
            if (this.templatePath) {
                this.__getTemplateAndRender();
            } else {
                if (this.dxDataGrid) {
                    this.__renderDxDataGrid();
                }
                // 生命周期-渲染之后
                this.__afterRender();
            }
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
                if (_this._conditionHtml.length || _this.conditionHtml.length) {
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
            new $.jqcFormToolBar({
                element: _this._toolBar,
                conditionHtml: _this._conditionHtml[0] || '',
                controlHtml: _this._controlHtml[0] || ''
            });
            setTimeout(function () {
                $.formUtil.format(_this._toolBar);
                if (!_this.dxDataGrid) {
                    return;
                }
                _this._toolBar.find('.toolbar-left button.queryBtn').click(function () {
                    var _data = $.formUtil.fetch(_this._toolBar);
                    _this.fillDxDataGrid(_data);
                });
            }, 0);
        };
        $.App.prototype.__renderDxDataGrid = function () {
            var _this = this;
            this._dxDataGrid = $('<div data-dx="a">');
            this._root.append(_this._dxDataGrid);
            var _dxConfig = JSON.parse(JSON.stringify(_this.dxDataGrid));
            var _columns = [].concat(_this._config.dxDataGridDefaultConfig.columns, _this.dxDataGrid.columns);
            var dxConfig = Object.assign({}, _this._config.dxDataGridDefaultConfig, _dxConfig, {columns: _columns});
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
            if (dx && this.dxDataGrid.exportProxyClassName) {
                this.root.find(`.${_this.dxDataGrid.exportProxyClassName}`)
                    .click(function(e) {
                        e.stopPropagation();
                        dx.exportToExcel(false);
                    });
            }
            // 搜索事件绑定
            if (dx && this.dxDataGrid.searchProxyClassName) {
                this.root.find(`.${_this.dxDataGrid.searchProxyClassName}`)
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
                _this.fillDxDataGridByData(res.result);
                _this.loading.hide();
            });
        };
        $.App.prototype.__renderContextMenu = function () {
            var _this = this;
            this._contextmenu = new $.jqcContextMenu(_this.contextmenu);
            setTimeout(function () {
                $(document).on('mousewheel.$App', function () {
                    _this._contextmenu.box && _this._contextmenu.box.remove();
                });
            }, 0);
        };
        $.App.prototype.__afterRender = function () {
            var _this = this;
            setTimeout(function () {
                _this.afterRender && _this.afterRender();
                _this.loading.hide();
            }, 0);
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
                setTimeout(function () {
                    params.afterRender && params.afterRender(_template, _dialog);
                    params.defaultData && $.formUtil.fill(_template, params.defaultData);
                }, 0);
                if (params.readOnly) {
                    _template.find('[databind]').attr('disabled', 'disabled');
                    _template.find('button').hide();
                }
                _dialog.open();
                $btn.click(function () {
                    var _data = $.formUtil.fetch(_template);
                    _data = Object.assign({}, params.defaultData, _data);
                    _this.requestPost(params.api, _data).then(res => {
                        if (res.code == 0) {
                            _dialog.close();
                            _this.triggerQuery();
                            if (params.success) {
                                params.success(res);
                            } else {
                                $.jqcNotification({
                                    type: 'success',
                                    title: '操作成功。'
                                });
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
        $.App.prototype.triggerQuery = function () {
            this._toolBar.find('.toolbar-left button.queryBtn').trigger('click');
        };
        $.App.prototype.confirm = function (params) {
            var _content = $('<div>');
            var _text = $('<div>').addClass('jqcConfirm-textBox');
            if (params.content) {
                _text.append(params.content);
            }
            var done = $('<button>').addClass('btn jqcConfirm-btn').text('确认');
            var cancel = $('<button>').addClass('btn jqcConfirm-btn').text('取消');
            var $btnBox = $('<div>')
                .addClass('jqcConfirm-btnBox')
                .append(done)
                .append(cancel);
            _content.append(_text).append($btnBox);
            var _confirm = new $.jqcDialog({
                title: params.title || '请确认',
                content: _content,
                width: params.width || 300,
                afterClose: function () {
                    params.onClose && params.onClose();
                }
            });
            _confirm.open();
            done.click(function () {
                params.onConfirm && params.onConfirm();
                _confirm.close();
            });
            cancel.click(function () {
                params.onCancel && params.onCancel();
                _confirm.close();
            })
        };
        $.App.prototype.delete = function (params) {
            var _this = this;
            this.confirm({
                title: params.title,
                content: params.content,
                onConfirm: function () {
                    if (!params.api || !params.data) {
                        throw new Error('delete方法缺少“api”或“data”属性！');
                    }
                    _this.requestPost(params.api, params.data).then(res => {
                        if (res.code == 0) {
                            $.jqcNotification({
                                type: 'success',
                                title: '删除成功。'
                            });
                            if (params.success) {
                                params.success();
                            } else {
                                _this.triggerQuery();
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
    });