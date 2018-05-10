;(function ($) {
    const config = {
        templateUrl: './views/form/form1.html',         //html模板文件路径

        dxDataGrid: {                                   //dx表格配置，有该配置则渲染
            exportProxyClassName: 'exportBtn',          //自定义导出按钮的类名（不需要带.）
            searchProxyClassName: 'searchInput',        //自定义搜索框的类名
            columns: []                                 //列配置，不需要配置序号列
        },

        contextmenu: {                                  //右击菜单配置  有该配置则渲染 配置同$.jqcContextMenu
            menus: [],
            onSelect: function (data) {
                console.log(data);
            }
        }
    };
    /**
     * 
     * @param { jQuery Object } root 渲染后页面对应的 jQuery 对象
     * @param { Object } dxDataGrid  页面内的dxDataGrid组件实例对象，可直接操作，等于$('selector').dxDataGrid('instance');组件详细操作参考dxDataGrid文档 
     */
    var afterRender = function (root, dxDataGrid) {     
        dxDataGrid.option('dataSource', mock);
        root.find('.btn1').click(function () {
            dxDataGrid.option('dataSource', json);
        });
    };

    $.renderForm({
        config,
        afterRender
    });
})(jQuery);