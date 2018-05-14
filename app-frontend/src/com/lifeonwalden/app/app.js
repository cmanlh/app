const LIB_ROOT_PATH = '../../../../libs/';
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
    .importScript('./js/dx/jszip.js')
    .importScript('./js/dx/dx.web.debug.js')
    .importScript('./js/dx/dx.messages.cn.js')
    .importCss('./css/dx/dx.common.css')
    .importCss('./css/dx/dx.light.css')
    // 全局配置
    .importScript('./js/config/config.js')
    // core
    .importScript('./js/common/common.core.js')
    .execute(function () {
        var pageWidth = window.innerWidth;
        var menuData = [{
                id: 'form1',
                label: '亚当斯密'
            },
            {
                id: 'form3',
                label: '力与反作用力'
            }
        ];

        var allMenuData = [{
            id: '1',
            label: '经济学',
            child: [{
                id: 'form1',
                label: '亚当斯密'
            }, {
                id: 'form2',
                label: '国富论'
            }]
        }, {
            id: '2',
            label: '物理学',
            child: [{
                id: 'form3',
                label: '力与反作用力'
            }, {
                id: 'form4',
                label: '浮力'
            }]
        }];

        var menus = new $.jqcMenu({
            data: menuData,
            top: 40,
            left: 0,
            autoSkip: true,
            allowedConfig: true,
            displayed: false,
            configBoxWidth: 500,
            configurableMenuData: allMenuData,
            onSelect: function (menu) {
                var _menu = {
                    id: menu.id,
                    text: menu.label
                };
                $.addForm(_menu, tab);
            },
            onResettingMenu: function (menu) {
                console.log(menu);
            }
        });
        // 触发第一个menu的点击事件
        setTimeout(function () {
            $('.jqcMenuLeaf').eq(0).trigger('click');
        }, 0);

        // show or hide menu
        var menuDisplayed = false;
        var $tab = $('.app-tab');
        $('.app-menu-switch').click(function () {
            if (menuDisplayed) {
                menus.hide();
                $tab.animate({
                    'padding-left': 0
                }, 200);
                $(this).text("Menu >>");
            } else {
                menus.show();
                $tab.animate({
                    'padding-left': 155
                }, 200);
                $(this).text('Menu <<');
            }
            menuDisplayed = !menuDisplayed;
        });

        var tab = new $.jqcTab({
            element: $('.app-tab'),
            position: 'absolute'
        });

        var msg = new $.jqcMsg({
            element: $('.app-msg'),
            data: [{
                text: '111111111111'
            }, {
                text: '2222222222222'
            }, {
                text: '3333333333333'
            }, {
                text: '444444444444'
            }, {
                text: '555555555555'
            }, {
                text: '66666666666666666'
            }, {
                text: '7777777777777'
            }, {
                text: '88888888'
            }, {
                text: '99999999999'
            }, {
                text: '0000000000000000'
            }],
            onSelect: function (data, callback) {
                alert(data.text);
                callback(true);
            }
        });
    });