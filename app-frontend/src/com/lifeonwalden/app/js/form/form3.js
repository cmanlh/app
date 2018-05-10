;(function ($) {
    console.log('form3第一次加载。。。');
    // 最后调用
    const config = {
        templateUrl: './views/form/form3.html'
    };

    var afterRender = function (root) {
        console.log('form3渲染完成。')
    };

    $.renderForm({
        config,
        afterRender,
    });
})(jQuery);