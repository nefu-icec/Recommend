$(document).ready(function () {

    checkAdminSession(function () {
        ReferrerManager.getAll(function (referrers) {
            for (var i in referrers) {
                var referrer = referrers[i];
                $("#referrer-list tbody").mengular(".referrer-list-template", {
                    rid: referrer.rid,
                    telephone: referrer.telephone,
                    name: referrer.name,
                    wechat: referrer.wechat,
                    password: referrer.password
                });

                if (referrer.applying) {
                    $("#" + referrer.rid + " .referrer-list-applying input").removeAttr("disabled");
                }

                $("#" + referrer.rid + " .referrer-list-applying input").bootstrapSwitch({
                    state: referrer.applying
                }).on("switchChange.bootstrapSwitch", function (event, state) {
                    if (state) {
                        return;
                    }
                    var rid = $(this).mengularId();
                    ReferrerManager.withdraw(rid, function (success) {
                        if (success) {

                        } else {
                            location.href = "session.html";
                        }
                    });

                });

            }
        });
    });

});