<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Encoding" charset="UTF-8" />
    <title><spring:message code="page.title"/></title>
    <link href="static/css/smoothness/jquery-ui-1.10.3.custom.css" rel="stylesheet">
    <script src="static/js/libs/jquery-1.9.1.js"></script>
    <script src="static/js/libs/jquery-ui-1.10.3.custom.js"></script>

    <style>
        .custom-combobox {
            position: relative;
            display: inline-block;
        }
        .custom-combobox-toggle {
            position: absolute;
            top: 0;
            bottom: 0;
            margin-left: -1px;
            padding: 0;
            /* support: IE7 */
            *height: 1.7em;
            *top: 0.1em;
        }
        .custom-combobox-input {
            margin: 0;
            padding: 0.3em;
        }
    </style>

    <script>
        (function( $ ) {
            $.widget( "custom.combobox", {
                _create: function() {
                    this.wrapper = $( "<span>" )
                            .addClass( "custom-combobox" )
                            .insertAfter( this.element );

                    this.element.hide();
                    this._createAutocomplete();
                    this._createShowAllButton();
                },

                _createAutocomplete: function() {
                    var selected = this.element.children( ":selected" ),
                            value = selected.val() ? selected.text() : "";

                    this.input = $( "<input>" )
                            .appendTo( this.wrapper )
                            .val( value )
                            .attr( "title", "" )
                            .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
                            .autocomplete({
                                delay: 0,
                                minLength: 0,
                                source: $.proxy( this, "_source" )
                            })
                            .tooltip({
                                tooltipClass: "ui-state-highlight"
                            });

                    this._on( this.input, {
                        autocompleteselect: function( event, ui ) {
                            ui.item.option.selected = true;
                            this._trigger( "select", event, {
                                item: ui.item.option
                            });
                        },

                        autocompletechange: "_removeIfInvalid"
                    });
                },

                _createShowAllButton: function() {
                    var input = this.input,
                            wasOpen = false;

                    $( "<a>" )
                            .attr( "tabIndex", -1 )
                            .attr( "title", "Show All Items" )
                            .tooltip()
                            .appendTo( this.wrapper )
                            .button({
                                icons: {
                                    primary: "ui-icon-triangle-1-s"
                                },
                                text: false
                            })
                            .removeClass( "ui-corner-all" )
                            .addClass( "custom-combobox-toggle ui-corner-right" )
                            .mousedown(function() {
                                wasOpen = input.autocomplete( "widget" ).is( ":visible" );
                            })
                            .click(function() {
                                input.focus();

                                // Close if already visible
                                if ( wasOpen ) {
                                    return;
                                }

                                // Pass empty string as value to search for, displaying all results
                                input.autocomplete( "search", "" );
                            });
                },

                _source: function( request, response ) {
                    var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
                    response( this.element.children( "option" ).map(function() {
                        var text = $( this ).text();
                        if ( this.value && ( !request.term || matcher.test(text) ) )
                            return {
                                label: text,
                                value: text,
                                option: this
                            };
                    }) );
                },

                _removeIfInvalid: function( event, ui ) {

                    // Selected an item, nothing to do
                    if ( ui.item ) {
                        return;
                    }

                    // Search for a match (case-insensitive)
                    var value = this.input.val(),
                            valueLowerCase = value.toLowerCase(),
                            valid = false;
                    this.element.children( "option" ).each(function() {
                        if ( $( this ).text().toLowerCase() === valueLowerCase ) {
                            this.selected = valid = true;
                            return false;
                        }
                    });

                    // Found a match, nothing to do
                    if ( valid ) {
                        return;
                    }

                    // Remove invalid value
                    this.input
                            .val( "" )
                            .attr( "title", value + " didn't match any item" )
                            .tooltip( "open" );
                    this.element.val( "" );
                    this._delay(function() {
                        this.input.tooltip( "close" ).attr( "title", "" );
                    }, 2500 );
                    this.input.data( "ui-autocomplete" ).term = "";
                },

                _destroy: function() {
                    this.wrapper.remove();
                    this.element.show();
                }
            });
        })( jQuery );

        $(function() {
            $( "#sendBt" ).button().click(
                    function( event ) {
                        $.ajax({
                            type: "POST",
                            url: "${applicationUrl}address/find",//todo url
                            data: "region="+document.getElementById("regionOptions").value+
                                    "&city="+document.getElementById("city").value+
                                    "&street="+document.getElementById("street").value,
                            success: function(pageResponse){
                                var tableAgr=document.getElementById("addressResultTable");
                                for (var i=tableAgr.rows.length-1; i>0; i--) {
                                    tableAgr.deleteRow(i);
                                }
                                $.each(pageResponse, function(index, obj){
                                    var rowAgr=tableAgr.insertRow(-1);

                                    var cell0Agr=rowAgr.insertCell(0);
                                    cell0Agr.innerHTML=obj.street;
                                    var cell1Agr=rowAgr.insertCell(1);
                                    cell1Agr.innerHTML=obj.cityType+" "+obj.city;
                                    var cell2Agr=rowAgr.insertCell(2);
                                    cell2Agr.innerHTML=obj.district;
                                    var cell3Agr=rowAgr.insertCell(3);
                                    cell3Agr.innerHTML=obj.region;
                                    var cell4Agr=rowAgr.insertCell(4);
                                    cell4Agr.innerHTML=obj.zipcode;
                                });
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.status+": "+thrownError);
                            }
                        });
                    });

            //city autocomplete, dynamically gets info from backend service
            $( "#city" ).autocomplete({
                source: function( request, response ) {
                    $.ajax({
                        type: "POST",
                        url: "${applicationUrl}address/find/city",//todo url
                        //dataType: "jsonp",
                        data: "region="+$('#regionOptions').val()+"&searchString="+document.getElementById("city").value,
                        success: function( data ) {
                            response( $.map( data, function( item ) {
                                return {
                                    label: item,
                                    value: item
                                }
                            }));
                        }
                    });
                },
                minLength: 2,
                open: function() {
                    $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
                },
                close: function() {
                    $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
                }
            });

            //street autocomplete, dynamically gets info from backend service
            $( "#street" ).autocomplete({
                source: function( request, response ) {
                    $.ajax({
                        type: "POST",
                        url: "${applicationUrl}address/find/street",//todo url
                        //dataType: "jsonp",
                        data: "region="+$('#regionOptions').val()+
                                "&city="+document.getElementById("city").value+
                                "&searchString="+document.getElementById("street").value,
                        success: function( data ) {
                            response( $.map( data, function( item ) {
                                return {
                                    label: item,
                                    value: item
                                }
                            }));
                        }
                    });
                },
                minLength: 2,
                open: function() {
                    $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
                },
                close: function() {
                    $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
                }
            });


            var options = $("#regionOptions");
            options.append($("<option />").val('').text('Не выбрано...'));
            <c:forEach var="region" items="${regions}">
                    options.append($("<option />").val('${region}').text('${region}'));
            </c:forEach>

            $( "#regionOptions" ).combobox();
        });

    </script>

</head>
<body>
<table>
    <tr>
        <div class="ui-widget">
            <label for="regionOptions">Область: </label>
            <select id="regionOptions"/>
        </div>
    </tr>
    <tr>
        <div class="ui-widget">
            <label for="city">Населенный пункт: </label>
            <input id="city" type="text" />
        </div>
    </tr>
    <tr>
        <div class="ui-widget">
            <label for="street">Улица: </label>
            <input id="street" type="text" />
        </div>
    </tr>
    <tr>
        <input type="button" id="sendBt" value="Найти адреса">

        <table id="addressResultTable" border="1">
            <tr>
                <th>Улица</th>
                <th>Населенный пункт</th>
                <th>Район</th>
                <th>Область</th>
                <th>Индекс</th>
            </tr>
        </table>
    </tr>
</table>
</body>
</html>