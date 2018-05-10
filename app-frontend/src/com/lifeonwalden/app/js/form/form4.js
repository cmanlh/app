;(function ($) {
    console.log('form4第一次加载。。。');
    // 最后调用
    const config = {
        templateUrl: './views/form/form4.html'
    };
    var afterRender = function (root) {
        console.log('form4渲染完成。');
        root.find('li').click(function () {
            alert($(this).text());
        });
    }
    $.renderForm({
        config,
        afterRender,
    });
})(jQuery);