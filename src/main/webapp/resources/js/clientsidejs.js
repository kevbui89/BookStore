$(document).ready(function () {

    var animation_elements = $.find('.animation-element');
    var web_window = $(window);

    function check_if_in_view() {
        var window_height = web_window.height();
        var window_top_position = web_window.scrollTop();
        var window_bottom_position = (window_top_position + window_height);

        $.each(animation_elements, function () {

            var element = $(this);
            var element_height = $(element).outerHeight();
            var element_top_position = $(element).offset().top;
            var element_bottom_position = (element_top_position + element_height);

            if ((element_bottom_position >= window_top_position) && (element_top_position <= window_bottom_position)) {
                element.addClass('in-view');
            }
        });

    }

    $(window).on('scroll resize', function () {
        check_if_in_view()
    })
    $(window).trigger('scroll');

    $(".post-module").hover(function () {
        $(this)
                .find(".description")
                .stop()
                .animate(
                        {
                            height: "toggle",
                            opacity: "toggle"
                        },
                        300
                        );
    });

    $('.card-front').hover(function () {
        $(this).toggleClass('flipped');
    });

    $("#wrapper").addClass("loaded");

    $(".more-info-kevin").click(function () {
        $("#card").toggleClass("flip");
    });

    $(".more-info-ale").click(function () {
        $("#card-ale").toggleClass("flip");
    });

    $(".more-info-werner").click(function () {
        $("#card-werner").toggleClass("flip");
    });

    $(".more-info-denis").click(function () {
        $("#card-denis").toggleClass("flip");
    });

    $(window).scroll(function () {
        $('#object').each(function () {
            var imagePos = $(this).offset().top;

            var topOfWindow = $(window).scrollTop();
            if (imagePos < topOfWindow + 400) {
                $(this).addClass("slideUp");
            }
        });
    });

    $(".hover").mouseleave(
            function () {
                $(this).removeClass("hover");
            }
    );

    /* Carousel sales */

    (function ($) {
        "use strict";

        var bindToClass = "scarousel",
                containerWidth = 0,
                scrollWidth = 0,
                posFromLeft = 0, // Stripe position from the left of the screen
                stripePos = 0, // When relative mouse position inside the thumbs stripe
                animated = null,
                $indicator,
                $carousel,
                el,
                $el,
                ratio,
                scrollPos,
                nextMore,
                prevMore,
                pos,
                padding;

        // calculate the thumbs container width
        function calc(e) {
            $el = $(this).find(" > .wrap");
            el = $el[0];
            $carousel = $el.parent();
            $indicator = $el.prev(".indicator");

            nextMore = prevMore = false; // reset

            containerWidth = el.clientWidth;
            scrollWidth = el.scrollWidth; // the "<ul>"" width
            padding = 0.2 * containerWidth; // padding in percentage of the area which the mouse movement affects
            posFromLeft = $el.offset().left;
            stripePos = e.pageX - padding - posFromLeft;
            pos = stripePos / (containerWidth - padding * 2);
            scrollPos = (scrollWidth - containerWidth) * pos;

            if (scrollPos < 0)
                scrollPos = 0;
            if (scrollPos > scrollWidth - containerWidth)
                scrollPos = scrollWidth - containerWidth;

            $el.animate({scrollLeft: scrollPos}, 200, "swing");

            if ($indicator.length)
                $indicator.css({
                    width: containerWidth / scrollWidth * 100 + "%",
                    left: scrollPos / scrollWidth * 100 + "%"
                });

            clearTimeout(animated);
            animated = setTimeout(function () {
                animated = null;
            }, 200);

            return this;
        }

        // move the stripe left or right according to mouse position
        function move(e) {
            // don't move anything until inital movement on 'mouseenter' has finished
            if (animated)
                return;

            ratio = scrollWidth / containerWidth;
            stripePos = e.pageX - padding - posFromLeft; // the mouse X position, "normalized" to the carousel position

            if (stripePos < 0)
                stripePos = 0;

            pos = stripePos / (containerWidth - padding * 2); // calculated position between 0 to 1
            // calculate the percentage of the mouse position within the carousel
            scrollPos = (scrollWidth - containerWidth) * pos;

            el.scrollLeft = scrollPos;
            if ($indicator[0] && scrollPos < scrollWidth - containerWidth)
                $indicator[0].style.left = scrollPos / scrollWidth * 100 + "%";

            // check if element has reached an edge
            prevMore = el.scrollLeft > 0;
            nextMore = el.scrollLeft < scrollWidth - containerWidth;

            $carousel.toggleClass("left", prevMore);
            $carousel.toggleClass("right", nextMore);
        }

        $.fn.carousel = function (options) {
            $(document)
                    .on("mouseenter.carousel", "." + bindToClass, calc)
                    .on("mousemove.carousel", "." + bindToClass, move);
        };

        // automatic binding to all elements which have the class that is assigned to "bindToClass"
        $.fn.carousel();
    })(jQuery);

    $('.book-reviews').find(".stars").each(function () {
        var content = $(this).html().trim();
        var stars = "";

        for (var i = 0; i < 5; i++) {
            if (content - i <= 0)
                stars += '<span class="fa fa-star-o"></span>';
            else
                stars += '<span class="fa fa-star"></span>';
        }
        $(this).html(stars);
    });

    $('.rating-stars-container').on("mouseleave", function () {
        var val = $(this).siblings().eq(0).val();
        if (val !== "") {
            $(this).children().each(function () {
                if ($(this).index() <= val - 1) {
                    $(this).removeClass('fa-star-o');
                    $(this).addClass('fa-star');
                } else {
                    $(this).removeClass('fa-star');
                    $(this).addClass('fa-star-o');
                }
            });

            $(this).addClass("rating-stars-container-selected");
        }
    });

    $('.rating-star').click(function () {
        var pos = $(this).index() + 1;

        $(this).parent().siblings().each(function () {
            $(this).val(pos);
        });

        $(this).parent().addClass("rating-stars-container-selected");
    });

    $(function () {
        $(".pref-image").click(function () {
            var $this = $(this);
            if ($this.hasClass('active')) {
                $this.removeClass('active');
            } else {
                $('.active').removeClass('active');
                $this.addClass('active');
            }
        });
    });

});


