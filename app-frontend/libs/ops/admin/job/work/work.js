;
(function($) {
    // 页面需要的展示内容
    const config = {
	    location : document.currentScript.src,
        templateUrl: 'work.html'
    };

    var afterRender = function(root) {
        dxDataGrid.option('dataSource', mock);

        root.find('.btn1').click(function() {
            dxDataGrid.option('dataSource', json);
        });
        console.log('form2渲染完毕。');
    };

    $.renderForm({
        config,
        afterRender
    });
})(jQuery);