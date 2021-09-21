<%-- russian localization for datatables --%>
<%@ attribute name="table" required="true" %>
<script type="text/javascript">
    var language = "${lang}";
    $(document).ready(function () {
        $('${table}').dataTable({
            "language": {
                "url": (language === 'ru') ? "script/russian.lang" : "",
            }
        });
    });
</script>
