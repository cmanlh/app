$JqcLoader.registerModule($JqcLoader.newModule('com.jquery', LIB_ROOT_PATH).registerComponents(['jquery', 'keycode', 'version']))
    .registerModule($JqcLoader.newModule('com.lifeonwalden.jqc', LIB_ROOT_PATH)
        .registerComponents(['baseElement', 'blocker', 'cache', 'dateUtil', 'dialog', 'draggable', 'format', 'inputNumber', 'lang'])
        .registerComponents(['loader', 'location', 'pinyin', 'resizeable', 'selectbox', 'slide', 'uniqueKey', 'slide', 'uniqueKey'])
        .registerComponents(['valHooks', 'zindex'])
        .registerComponents(['toolkit'])
        .registerComponents(['menu'])
        .registerComponents(['contextmenu'])
        .registerComponents(['loading'])
        .registerComponents(['formToolBar', 'formUtil', 'datetimepicker', 'tip', 'msg', 'tab'])
        .registerComponents(['apisBox']));

$JqcLoader.importComponents('com.jquery', ['jquery', 'keycode', 'version'])
    .importComponents('com.lifeonwalden.jqc', ['menu', 'formUtil', 'msg', 'tab', 'formToolBar', 'contextmenu', 'toolkit', 'loading'])
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
        const formCache = new Map();
        const T = $.jqcToolkit;
        // set cache
        $.renderForm = function(data) {
            if (data && data.config) {
                formCache.set($._globalCacheId, data);
            } else {
                throw new Error('$.renderForm: must has property "config".');
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

        const config = $.getTemplateClassNameSetting();
        const dxDataGridDefaultConfig = $.getDxDataGridDefaultConfig();
        const loading = new $.jqcLoading();
        $.FormPage = function(box, params) {
            this.box = box;
            this.config = params.config;
            this.html = '';
            this._afterRender = params.afterRender;
            this.beforeDestroy = params.beforeDestroy;
            this.dxDataGridConfig = params.config.dxDataGrid;
            this.contextmenuConfig = params.config.contextmenu;
            this.init();
        };

        $.FormPage.prototype.init = function() {
            var _this = this;
            this.createContextMenu();
            // 是否存在网页模板
            if (this.config.templateUrl) {
                getHtml(_this.config.location, _this.config.templateUrl).then(res => {
                    var html = $(res);
                    _this.conditionHtml = html.find(`.${config.conditionHtmlClassName}`);
                    _this.controlHtml = html.find(`.${config.controlHtmlClassName}`);
                    _this.contentHtml = html.find(`.${config.contentHtmlClassName}`);
                    // 是否渲染toolbar
                    if (_this.conditionHtml.length || _this.controlHtml.length) {
                        _this.box.prepend(_this.createToolbarBox());
                        new $.jqcFormToolBar({
                            element: _this.toolBarBox,
                            conditionHtml: _this.conditionHtml[0] || '',
                            controlHtml: _this.controlHtml[0] || ''
                        });
                    }
                    if (_this.contentHtml.length && _this.contentHtml[0].childNodes.length) {
                        _this.box.append(_this.contentHtml);
                    }
                    // 是否生成dxDataGrid
                    if (this.config.dxDataGrid && this.config.dxDataGrid.columns) {
                        _this.box.append(_this.createDxDataGridBox());
                        _this.renderDxDataGrid();
                    }
                    // 导出
                    var dx = _this.getDxDataGrid();
                    if (dx && _this.config.dxDataGrid && typeof(_this.config.dxDataGrid.exportProxyClassName) === 'string') {
                        _this.box.find(`.${_this.config.dxDataGrid.exportProxyClassName}`).click(function(e) {
                            e.stopPropagation();
                            dx.exportToExcel(false);
                        });
                    }
                    // 搜索
                    if (dx && _this.config.dxDataGrid && typeof(_this.config.dxDataGrid.searchProxyClassName) === 'string') {
                        _this.box.find(`.${_this.config.dxDataGrid.searchProxyClassName}`).keyup(function(e) {
                            e.stopPropagation();
                            var val = $(this).val();
                            dx.searchByText(val);
                        });
                    }
                    _this.afterRender();
                }).catch(res => {
                    loading.hide();
                });
            } else {
                // 是否生成dxDataGrid
                if (this.config.dxDataGrid && this.config.dxDataGrid.columns) {
                    _this.box.append(_this.createDxDataGridBox());
                    _this.renderDxDataGrid();
                }
                _this.afterRender();
            }
        };
        $.FormPage.prototype.afterRender = function() {
                var _this = this;
                loading.hide();
                this._afterRender && this._afterRender(_this.box, _this.getDxDataGrid());
            },
            $.FormPage.prototype.createToolbarBox = function() {
                var _this = this;
                this.toolBarBox = $('<div>');
                return this.toolBarBox;
            };

        $.FormPage.prototype.createDxDataGridBox = function() {
            var _this = this;
            this.dxDataGridBox = $('<div>').text(456);
            return this.dxDataGridBox;
        };

        $.FormPage.prototype.renderDxDataGrid = function() {
            var _this = this;
            this.config.dxDataGrid.columns.unshift(dxDataGridDefaultConfig.columns[0]);
            var dxConfig = Object.assign({}, dxDataGridDefaultConfig, _this.config.dxDataGrid);
            if (this.contextmenu) {
                dxConfig.onContextMenuPreparing = function(e) {
                    e.jQueryEvent.preventDefault();
                    if (e.row.rowType !== 'data') {
                        return;
                    }
                    _this.contextmenu.show(e.row.data);
                }
            }
            this.dxDataGridBox.dxDataGrid(dxConfig);
            return this.dxDataGridBox;
        };

        $.FormPage.prototype.getDxDataGrid = function() {
            return this.dxDataGridBox ? this.dxDataGridBox.dxDataGrid('instance') : null;
        };

        $.FormPage.prototype.createContextMenu = function() {
            var _this = this;
            if (this.config.contextmenu && this.config.contextmenu.menus && this.config.contextmenu.menus.length) {
                this.contextmenu = new $.jqcContextMenu(_this.config.contextmenu);
                setTimeout(function() {
                    $(document).on('mousewheel.FormPage', function() {
                        _this.contextmenu.box && _this.contextmenu.box.hide();
                    });
                }, 0);
            }
        };

        function getHtml(location, url) {
            return new Promise((resolve, reject) => {
                $.ajax(location.substr(0, location.lastIndexOf('/') + 1).concat(url), {
                    success: function(res) {
                        resolve(res);
                    },
                    error: function(res) {
                        reject(res);
                    }
                });
            });
        }

        $.addForm = function(menu, tab) {
            loading.show();
            var uid = menu.id;
            var text = menu.text;

            // tab内存在
            if (tab.index.has(uid)) {
                loading.hide();
                tab.add({ id: uid });
                return;
            }
            // 第一次点击menu
            if (!$.formCacheHas(uid)) {
                $._globalCacheId = uid;
                $JqcLoader
                    .importScript(APP_ROOT_PATH.concat(menu.url))
                    .execute(function() {
                        addTabAndCreatePage();
                    });
                return;
            } else {
                // 关闭后再次点击menu
                addTabAndCreatePage();
            }

            function addTabAndCreatePage() {
                tab.add({
                    id: uid,
                    title: text,
                    content: `<div data-tabid=${uid}></div>`
                });
                setTimeout(function() {
                    new $.FormPage($('div[data-tabid=' + uid + ']'), $.getFormCache(uid));
                }, 0);

            }
        };
    });