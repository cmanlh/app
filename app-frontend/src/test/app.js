const LIB_ROOT_PATH = '../../libs/';
const APP_ROOT_PATH = './';

$JqcLoader.importScript(LIB_ROOT_PATH.concat('com/lifeonwalden/app/js/common.js'))
    .execute(function() {
        var pageWidth = window.innerWidth;
        var menuData = [{
            id: 'form1',
            label: '亚当斯密',
            form: 'form/form1/form1.js'

        }, {
            id: 'form3',
            label: '力与反作用力',
            form: 'form/form2/form2.js'
        }];

        var allMenuData = [{
            id: '1',
            label: '经济学',
            child: [{
                id: 'form1',
                label: '亚当斯密',
                form: 'form/form1/form1.js'
            }, {
                id: 'form2',
                label: '国富论',
                form: 'form/form3/form3.js'
            }]
        }, {
            id: '2',
            label: '物理学',
            child: [{
                id: 'form3',
                label: '力与反作用力',
                form: 'form/form2/form2.js'
            }, {
                id: 'form4',
                label: '浮力',
                form: 'form/form4/form4.js'
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
            onSelect: function(menu) {
                var _menu = {
                    id: menu.id,
                    text: menu.label,
                    url: menu.form
                };
                $.addForm(_menu, tab);
            },
            onResettingMenu: function(menu) {
                console.log(menu);
            }
        });
        // 触发第一个menu的点击事件
        setTimeout(function() {
            $('.jqcMenuLeaf').eq(0).trigger('click');
        }, 0);

        // show or hide menu
        var menuDisplayed = false;
        var $tab = $('.app-tab');
        $('.app-menu-switch').click(function() {
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
            onSelect: function(data, callback) {
                alert(data.text);
                callback(true);
            }
        });
    });