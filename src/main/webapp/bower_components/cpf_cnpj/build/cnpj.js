!function (r) {
    var t = ["11111111111111", "22222222222222", "33333333333333", "44444444444444", "55555555555555", "66666666666666", "77777777777777", "88888888888888", "99999999999999"], n = function (r) {
        var t = 2, n = r.split("").reduce(function (r, t) {
            return [parseInt(t, 10)].concat(r)
        }, []), e = n.reduce(function (r, n) {
            return r += n * t, t = 9 === t ? 2 : t + 1, r
        }, 0), u = e % 11;
        return 2 > u ? 0 : 11 - u
    }, e = {};
    e.format = function (r) {
        return this.strip(r).replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, "$1.$2.$3/$4-$5")
    }, e.strip = function (r) {
        return (r || "").toString().replace(/[^\d]/g, "")
    }, e.isValid = function (r) {
        var e = this.strip(r);
        if (!e)return !1;
        if (14 !== e.length)return !1;
        if (t.indexOf(e) >= 0)return !1;
        var u = e.substr(0, 12);
        return u += n(u), u += n(u), u.substr(-2) === e.substr(-2)
    }, e.generate = function (r) {
        for (var t = "", e = 0; 12 > e; e++)t += Math.floor(9 * Math.random());
        return t += n(t), t += n(t), r ? this.format(t) : t
    }, r ? module.exports = e : window.CNPJ = e
}("undefined" != typeof exports);
