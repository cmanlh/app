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
        var styleCache = {};
        function Storage() {
            var _this = this;
            this.apiMap = COMMON_DATA_API;
            this.pinyinParser = new $.jqcPinyin();
            Promise.all([
                _this.get('system'),
                _this.get('department'),
                _this.get('user')
            ]).then(res => {
                _this.data = res;
            });
        }
        // 取数据
        Storage.prototype.get = function (name) {
            var _this = this;
            // var data = window.localStorage.getItem(name);
            var data = window.sessionStorage.getItem(name);
            var result;
            return new Promise((resolve, reject) => {
                if (data) {
                    try {
                        result = JSON.parse(data);
                    } catch (error) {
                        result = data;
                    }
                    resolve(result);
                } else {
                    // 从网络获取并存储
                    _this.getDataFromNet(name).then(res => {
                        resolve(res);
                    }).catch(err => {
                        console.error(err);
                    });
                }
            });
        }
        // 存数据
        Storage.prototype.set = function (name, data) {
            var _data;
            try {
                _data = JSON.stringify(data);
            } catch (error) {
                _data = data;
            }
            // window.localStorage.setItem(name, _data);
            window.sessionStorage.setItem(name, _data);
        }
        // 从网络获取数据
        Storage.prototype.getDataFromNet = function (name) {
            var _this = this;
            var url = this.apiMap[name];
            if (!url) {
                throw new Error(`api.${name} is undefined.`);
            }
            return new Promise((resolve, reject) => {
                $.ajax(url).then(res => {
                    if (res.code == 0) {
                        this.set(name, res.result);
                        resolve(res.result);
                    } else {
                        reject(res.msg);
                    }
                })
            });
        }
        // 格式化
        Storage.prototype.format = function($box) {
            var _this = this;
            var $els = $box.find('[commondata]');
            var $subs = $box.find('[nextchain]');
            var ignore = [];
            $.each($subs, function (index, el) {
                var nextchain = $(el).attr('nextchain');
                ignore.push(nextchain);
            });
            $.each($els, function (index, el) {
                var dataName = $(el).attr('commondata');
                if (!ignore.includes(dataName)) {
                    _this.formatSingle($(el), $box);
                }
            });
        }
        // 单个格式化
        Storage.prototype.formatSingle = function($node, $box) {
            var _this = this;
            var dataName = $node.attr('commondata');
            var defaultValue = $node.attr('defaultvalue');
            var nextchain = $node.attr('nextchain');
            var config = {
                element: $node,
                dataName: dataName + (defaultValue == '*' ? '_all' : ''),
                supportFuzzyMatch: true,
                supportPinYin: true,
                pinyinParser: this.pinyinParser,
                width: 200,
                defaultVal: defaultValue,
                onSelect: function (data) {
                    $node.trigger('change', data);
                    if (nextchain) {
                        var $subNode = $box.find(`[commondata=${nextchain}]`);
                        var _params = {};
                        if (dataName == 'department' && nextchain == 'user') {
                            _params.departmentId = data.departmentId;
                            $subNode.attr('filtervalue', data.departmentId);
                        }
                        _this.formatSingle($subNode, $box);
                    }
                }
            };
            config.optionData = {};
            this.get(dataName).then(data => {
                // 如果是筛选用户
                var filterName = $node.attr('filtername');
                var filterValue = $node.attr('filtervalue');
                if (filterName != undefined && filterValue != undefined) {
                    config.optionData.data = data.filter(item => (item[filterName] == filterValue));
                } else {
                    config.optionData.data = data;
                }
                // 系统
                if (dataName == 'system') {
                    config.optionData.adapter = {
                        value: 'id',
                        label: 'systemName',
                        filter: 'id',
                        pinyinFilter: 'systemName'
                    };
                    if (defaultValue == '*') {
                        config.extOption = [{
                            id: '*',
                            systemName: '全部'
                        }];
                    }
                }
                // 部门
                if (dataName == 'department') {
                    config.optionData.adapter = {
                        value: 'departmentId',
                        label: 'departmentName',
                        filter: 'departmentId',
                        pinyinFilter: 'departmentName'
                    };
                }
                // 用户
                if (dataName == 'user') {
                    config.optionData.adapter = {
                        value: 'userID',
                        label: 'displayName',
                        filter: 'userID',
                        pinyinFilter: 'displayName'
                    }
                    if (filterValue) {
                        config.dataName = 'user_' + filterValue;
                    }
                }
                new $.jqcSelectBox(config);
            });
        }
        var storage = new Storage();

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
            var _this = this;
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
            if (this.stylePath) {
                var _path = this.getAbsolutePath(_this.stylePath);
                if (!styleCache[_path]) {
                    $JqcLoader.importCss(_path).execute(function () {
                        styleCache[_path] = true;
                    });
                }
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
            new $.jqcFormToolBar({
                element: _this._toolBar,
                conditionHtml: _this._conditionHtml[0] || '',
                controlHtml: _this._controlHtml[0] || '',
                height: 40
            });
            setTimeout(function () {
                storage.format(_this._toolBar);
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
                storage.format(_template);
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
        $.App.prototype.createSelectBox = function (params) {
            var _this = this;
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
            new $.jqcSelectBox(config);
        };
        $.App.prototype.getSystemNameById = function(id) {
            var _this = this;
            var _data = storage.data[0].filter(item => (id === item.id));
            return _data.length ? _data[0].systemName : '';
        };
        $.App.prototype.getSystemInfoById = function(id) {
            var _this = this;
            var _data = storage.data[0].filter(item => (id === item.id));
            return _data;
        };
        $.App.prototype.getDepartmentNameById = function(id) {
            var _this = this;
            var _data = storage.data[1].filter(item => (id === item.departmentId));
            return _data.length ? _data[0].departmentName : '';
        };
        $.App.prototype.getDepartmentInfoById = function(id) {
            var _this = this;
            var _data = storage.data[1].filter(item => (id === item.departmentId));
            return _data;
        };
        $.App.prototype.getUserNameById = function(id) {
            var _this = this;
            var _data = storage.data[2].filter(item => (id === item.userID));
            return _data.length ? _data[0].displayName : '';
        };
        $.App.prototype.getUserInfoById = function(id) {
            var _this = this;
            var _data = storage.data[2].filter(item => (id === item.userID));
            return _data;
        };
        $.App.prototype.getUsersByDepartmentId = function(id) {
            var _this = this;
            var _data = storage.data[2].filter(item => (id === item.departmentId));
            return _data;
        };
    });