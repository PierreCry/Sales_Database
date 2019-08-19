<!DOCTYPE html>

<html>

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Projet J2E - PMD</title>

        <script	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/0.8.1/mustache.min.js"></script>
        
        <style>
            
        #StyleTable {
            font-family: 'Roboto', sans-serif;
            width: 100%;
            margin: 0 0 40px 0;
            box-shadow: 0 1px 3px rgba(0,0,0,0.2);
        }

        #StyleTable td, #StyleTable th {
            text-align: center; 
            vertical-align: middle;
            border: 1px solid #ddd;
            padding: 8px;
        }

        #StyleTable tr:nth-child(even){background-color: #f2f2f2;}

        #StyleTable tr:hover {background-color: #ddd;}

        #StyleTable th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #1abc9c;
            color: white;
        }

        #codeForm {
            width: 450px;
            font-size: 16px;
            background: #1abc9c;
            margin: 10px auto;
            padding: 30px 30px 15px 30px;
            border: 5px solid #53687E;
        }

        #button {
            position: relative;
            display: block;
            padding: 19px 39px 18px 39px;
            color: #FFF;
            margin: 0 auto;
            background: #1abc9c;
            font-size: 18px;
            text-align: center;
            font-style: normal;
            width: 100%;
            border: 1px solid #16a085;
            border-width: 1px 1px 3px;
            margin-bottom: 10px;
        }

        #button:hover {
            background: #109177;
        }
            
        .form-style-5 {
            max-width: 550px;
            padding: 10px 20px;
            margin: 10px auto;
            padding: 20px;
            background: #ccc;
            border-radius: 8px;
            font-family: 'Roboto', sans-serif;
        }
        
        .form-style-5 fieldset {
            border: none;
        }
        
        .form-style-5 legend {
            font-size: 1.4em;
            margin-bottom: 10px;
            margin-top: 35px;
        }
        
        .form-style-5 label {
            display: block;
            margin-bottom: 8px;
        }
        
        .form-style-5 input[type="text"],
        .form-style-5 input[type="date"],
        .form-style-5 input[type="datetime"],
        .form-style-5 input[type="email"],
        .form-style-5 input[type="number"],
        .form-style-5 input[type="search"],
        .form-style-5 input[type="time"],
        .form-style-5 input[type="url"],
        .form-style-5 textarea,
        .form-style-5 select {
            font-family: Georgia, 'Roboto', sans-serif;
            background: rgba(255,255,255,.1);
            border: none;
            border-radius: 4px;
            font-size: 15px;
            margin: 0;
            outline: 0;
            padding: 10px;
            width: 100%;
            box-sizing: border-box; 
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box; 
            background-color: #e8eeef;
            color:#8a97a0;
            -webkit-box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
            box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
            margin-bottom: 30px;
        }
        
        .form-style-5 input[type="text"]:focus,
        .form-style-5 input[type="date"]:focus,
        .form-style-5 input[type="datetime"]:focus,
        .form-style-5 input[type="email"]:focus,
        .form-style-5 input[type="number"]:focus,
        .form-style-5 input[type="search"]:focus,
        .form-style-5 input[type="time"]:focus,
        .form-style-5 input[type="url"]:focus,
        .form-style-5 textarea:focus,
        .form-style-5 select:focus {
            background: #d2d9dd;
        }
        
        .form-style-5 select {
            -webkit-appearance: menulist-button;
            height:35px;
        }
        
        .form-style-5 .number {
            background: #1abc9c;
            color: #fff;
            height: 30px;
            width: 30px;
            display: inline-block;
            font-size: 0.8em;
            margin-right: 4px;
            line-height: 30px;
            text-align: center;
            text-shadow: 0 1px 0 rgba(255,255,255,0.2);
            border-radius: 15px 15px 15px 0px;
        }

        .form-style-5 input[type="submit"],
        .form-style-5 input[type="button"] {
            position: relative;
            display: block;
            padding: 19px 39px 18px 39px;
            color: #FFF;
            margin: 0 auto;
            background: #1abc9c;
            font-size: 18px;
            text-align: center;
            font-style: normal;
            width: 100%;
            border: 1px solid #16a085;
            border-width: 1px 1px 3px;
            margin-bottom: 10px;
        }
        
        .form-style-5 button {
            position: relative;
            display: inline-block;
            padding: 19px 39px 18px 39px;
            color: #FFF;
            margin: 0 auto;
            background: #1abc9c;
            text-align: center;
            font-style: normal;
            width: 30%;
            border: 1px solid #16a085;
            border-width: 1px 1px 3px;
            margin-bottom: 10px;
        }
        
        .form-style-5 input[type="submit"]:hover,
        .form-style-5 input[type="button"]:hover,
        .form-style-5 button {
            background: #109177;
        }

        body {
            background-color: #eee;
            font-family: 'Roboto', sans-serif;
        }
        input:-moz-read-only {
            background-color: yellow;
        }

        input:read-only {
            background-color: yellow;
        }

        h1 {
            text-transform: uppercase;
            font-weight: bold;
        }

        .center, h1 {
            color: #1abc9c;
            text-align: center;
            padding-bottom: 25px;
        }
            
        </style>
        
        <script>
            
            $(document).ready(
                function () {
                    document.getElementById('codeForm').style.display = 'none';
                }
            );

            function showCodesPO() {
                $.ajax({
                    url: "allPO",
                    dataType: "json",
                    error: showError,
                    success:
                        function (result) {
                            var template = $('#codesTemplate1').html();
                            var processedTemplate = Mustache.to_html(template, result);
                            $('#affPO').html(processedTemplate);
                            $('#affP').html("");
                            document.getElementById('codeForm').style.display = 'block';
                        }
                });
            }

            function showCodesP() {
                $.ajax({
                    url: "allP",
                    dataType: "json",
                    error: showError,
                    success:
                            function (result) {
                                var template = $('#codesTemplate2').html();
                                var processedTemplate = Mustache.to_html(template, result);
                                $('#affP').html(processedTemplate);
                                $('#affPO').html("");
                                document.getElementById('codeForm').style.display = 'none';
                            }
                });
            }

            function addPO(Product_ID) {
                
                var quantity = $('#'+Product_ID+'_quantity').val();
                
                $.ajax({
                    url: "addPO",
                    data: {"Product_ID":Product_ID,"quantity":quantity,"id":'${sessionScope.id}'},
                    dataType: "json",
                    success:
                        function () {
                            showCodesPO();
                        },
                    error: showError
                });
                return false;
                
            }

            function delPO(order_num) {
                $.ajax({
                    url: "delPO",
                    data: {"order_num": order_num},
                    dataType: "json",
                    error: showError,
                    success:
                        function () {
                            showCodesPO();
                        }
                });
                return false;
            }
            
            function modifPO() {
                $.ajax({
                    url: "modifPO",
                    data: $("#codeForm").serialize(),
                    dataType: "json",
                    success: 
                        function () {
                            showCodesPO();
                        },
                    error: showError
                });
                return false;
            }

            function showError(xhr, status, message) {
                alert(JSON.parse(xhr.responseText).message);
            }

        </script>

    </head>

    <body>

        <h1>Voici vos commandes, ${sessionScope.name}</h1>
        
        <div class="form-style-5">
            <form>
                <input type="button" onclick="showCodesPO()" value="Afficher la table des commandes">
                <input type="button" onclick="showCodesP()" value="Passez une nouvelle commande">
            </form>
        </div>

        <div id="affPO"></div>
        <div id="affP"></div>

        <div class="form-style-5">
            <form id="codeForm" onsubmit="event.preventDefault(); modifPO();">
                Numero de commande : <br>
                <input id="order_num" name="order_num" size='50'><br><br>
                Quantite : <br>
                <input id="quantity" name="quantity" size='50'><br><br>
                <input type="submit" value="Modifier">
            </form> 
        </div>

        <div class="form-style-5">
            <form method="POST">
                <input id="button" type="SUBMIT" name="action" value="Vos informations">
                <input id="button" type="SUBMIT" name="action" value="Deconnexion">
            </form>
        </div>

        <script id="codesTemplate1" type="text/template">
            
            <table id="StyleTable">
            
                <tr>
                    <th>Numero de commande</th>
                    <th>Numero de client</th>
                    <th>Numero du produit</th>
                    <th>Quantité</th>
                    <th>Prix</th>
                    <th>Date de vente</th>
                    <th>Date de livraison</th>
                    <th>Action</th>
                </tr>
                
                {{#records}}
                    <tr>
                        <td>{{order_num}}</td>
                        <td>{{customer_id}}</td>
                        <td>{{product_id}}</td>
                        <td>{{quantity}}</td>
                        <td>{{shipping_cost}}</td>
                        <td>{{sales_date}}</td>
                        <td>{{shipping_date}}</td>
                        <th>
                            <button onclick="delPO('{{order_num}}')">Supprimer</button>
                        </th>
                    </tr>
                {{/records}}
            
            </table>
             
        </script>

        <script id="codesTemplate2" type="text/template">
            
            <table id="StyleTable">
            
                <tr>
                    <th>Numero du produit</th>
                    <th>Numero du fournisseur</th>
                    <th>Code du produit</th>
                    <th>Prix</th>
                    <th>Quantité disponible</th>
                    <th>Balisage</th>
                    <th>Disponible</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
                
                {{#records}}
                    <tr>
                        <td>{{Product_ID}}</td>
                        <td>{{Manufacturer_ID}}</td>
                        <td>{{Product_Code}}</td>
                        <td>{{Purchase_Cost}}</td>
                        <td>{{Quantity_on_hand}}</td>
                        <td>{{markup}}</td>
                        <td>{{available}}</td>
                        <td>{{Description}}</td>
                        <th>
                            <button onclick="addPO('{{Product_ID}}')">Commander</button>
                            <input id="{{Product_ID}}_quantity" type="number">
                        </th>
                    </tr>
                {{/records}}
            
            </table>
            
        </script>

    </body>

</html>

