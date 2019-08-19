<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <title>Les Statistiques</title>
    <!-- On charge JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <!-- On charge l'API Google -->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['table']});
        google.charts.setOnLoadCallback(ChartsCustomer);
        function drawTable(dataArray) {
            var data = new google.visualization.DataTable(dataArray);
            data.addColumn('string', 'Name');
            data.addColumn('number', 'Ventes');
            for (var i = 1; i < dataArray.length; i++) {
                data.addRows([[dataArray[i][0], dataArray[i][1]]]);
            }

            var table = new google.visualization.Table(document.getElementById('table_div'));
            table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
        }

        // Afficher les ventes par client
        function ChartsCustomer() {
            $.ajax({
                url: "salesCustomer",
                data: $("#guessForm").serialize(),
                dataType: "json",
                success: // La fonction qui traite les résultats
                        function (result) {
                            // On reformate le résultat comme un tableau
                            var chartData = [];
                            // On met le descriptif des données
                            chartData.push(["Client", "Ventes en €"]);
                            for (var client in result.records) {
                                chartData.push([client, result.records[client]]);
                            }
                            // On dessine le graphique
                            drawTable(chartData);
                        },
                error: showError
            });
        }

        google.charts.load('current', {
            'packages': ['geochart'],
            // Note: you will need to get a mapsApiKey for your project.
            // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
            'mapsApiKey': 'AIzaSyD-9tSrke72PouQMnMX-a7eZSW0jkFMBWY'
        });
        google.charts.setOnLoadCallback(ChartsState);
        function drawRegionsMap(dataArray) {
            var data = google.visualization.arrayToDataTable(dataArray);
            var options = {
                region: 'US',
                colorAxis: {colors: ['#00853f', 'black', '#e31b23']},
                resolution: 'provinces',
                backgroundColor: '#81d4fa',
                datalessRegionColor: '#f8bbd0',
                defaultColor: '#f5f5f5',
                height:300,
                width:550,
            };
            var chart = new google.visualization.GeoChart(document.getElementById('geochart-colors'));
            chart.draw(data, options);
        }

        // Afficher les ventes par Etat
        function ChartsState() {
            $.ajax({
                url: "salesState",
                data: $("#guessForm").serialize(),
                dataType: "json",
                success: // La fonction qui traite les résultats
                        function (result) {
                            // On reformate le résultat comme un tableau
                            var chartData = [];
                            // On met le descriptif des données
                            chartData.push(["Etats", "Ventes en € "]);
                            for (var client in result.records) {
                                chartData.push([client, result.records[client]]);
                            }
                            // On dessine le graphique
                            drawRegionsMap(chartData);
                        },
                error: showError
            });
        }

        google.charts.load('current', {packages: ['corechart', 'bar']});
        google.charts.setOnLoadCallback(ChartsProducts);
        function drawBasic(dataArray) {

            var data = new google.visualization.DataTable(dataArray);
            data.addColumn('string', 'Produits');
            data.addColumn('number', 'Ventes');
            for (var i = 1; i < dataArray.length; i++) {
                data.addRows([[dataArray[i][0], dataArray[i][1]]]);
            }

            var options = {
                title: "Chiffre d'affaires par produits",
                hAxis: {
                    title: 'Produits',
                },
                vAxis: {
                    title: 'Ventes en Euros'
                },
                height:300,
                width:550,
            };
            var chart = new google.visualization.ColumnChart(
                    document.getElementById('chart_div'));
            chart.draw(data, options);
        }

        // Afficher les ventes par catégorie de produits
        function ChartsProducts() {
            $.ajax({
                url: "salesProduct",
                data: $("#guessForm").serialize(),
                dataType: "json",
                success: // La fonction qui traite les résultats
                        function (result) {
                            // On reformate le résultat comme un tableau
                            var chartData = [];
                            // On met le descriptif des données
                            chartData.push(["Produits", "Ventes en €"]);
                            for (var client in result.records) {
                                chartData.push([client, result.records[client]]);
                            }
                            // On dessine le graphique
                            drawBasic(chartData);
                        },
                error: showError
            });
        }

        // Fonction qui traite les erreurs de la requête
        function showError(xhr, status, message) {
            alert("Erreur: " + status + " : " + message);
        }

        //Fonctions qui appelle les 3 charts en même temps
        function allCharts() {
            ChartsCustomer();
            ChartsState();
            ChartsProducts();
        }
        
        //Fonction qui affiche/enlève la charts des clients
        function CustoCA() {
            if (document.getElementById('client').style.display == 'none') {
                document.getElementById('client').style.display = 'block';
                document.getElementById('etat').style.display = 'none';
                document.getElementById('produit').style.display = 'none';
            } else {
                document.getElementById('client').style.display = 'none';
            }
        }

        //Fonction qui affiche/enlève la charts des états
        function StateCA() {
            if (document.getElementById('etat').style.display == 'none') {
                document.getElementById('etat').style.display = 'block';
                document.getElementById('client').style.display = 'none';
                document.getElementById('produit').style.display = 'none';
            } else {
                document.getElementById('etat').style.display = 'none';
            }
        }

        //Fonction qui affiche/enlève la charts des produits
        function ProductCA() {
            if (document.getElementById('produit').style.display == 'none') {
                document.getElementById('produit').style.display = 'block';
                document.getElementById('etat').style.display = 'none';
                document.getElementById('client').style.display = 'none';
            } else {
                document.getElementById('produit').style.display = 'none';
            }
        }

    </script>

    <style type="text/css">
        .form-style-5{
            max-width: 550px;
            padding: 10px 20px;
            margin: 10px auto;
            padding: 20px;
            background: #ccc;
            border-radius: 8px;
            font-family: 'Roboto', sans-serif;
        }
        .form-style-5 fieldset{
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
        .form-style-5 select:focus{
            background: #d2d9dd;
        }
        .form-style-5 select{
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
        .form-style-5 input[type="button"]
        {
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
        
        .form-style-5 button
        {
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
        .form-style-5 button
        {
            background: #109177;
        }

        
        body{
            background-color: #eee;
            font-family: 'Roboto', sans-serif;
        }
        input:-moz-read-only { /* For Firefox */
            background-color: yellow;
        }

        input:read-only {
            background-color: yellow;
        }

        h1{
            text-transform: uppercase;
            font-weight: bold;
        }

        .center, h1{
            color: #1abc9c;
            text-align: center;
            padding-bottom: 25px;
        }
        
    </style>
</head>
<body>
    <h1> Les statistiques </h1>

    <div class="center">
        <form id="guessForm" method="POST" accept-charset="UTF-8" >
            Date de début : 
            <input type="date" id='debut' value="2010-05-06" name="debut">
            Date de fin : 
            <input type="date" id='fin' value="2018-05-06" name="fin">
            <button type="button" onclick="allCharts()"> Vérifier </button>
        </form>
        <div class="form-style-5">
            <h2>Quelle statistique voulez-vous sélectionner ?</h2>
            <button type="button" onclick="CustoCA()"> Les Clients </button>
            <button type="button" onclick="StateCA()"> Les Etats </button>
            <button type="button" onclick="ProductCA()"> Les Produits </button>
        </div>
    </div>

    <!-- Les graphiques apparaît ici -->

    <div class="form-style-5">
    <div>    
        <form>
            
                
                <div id="client" style="display:none;">
                    <legend style="margin-top:0px;"><span class="number">1</span>Chiffres d'affaires des Clients</legend>
                    <div>
                        <div id="table_div" style="width: 550px; height: 300px;"></div>
                    </div>
                </div>
                
                <div id="etat"  style="display:none;">
                    <legend style="margin-top:0px;"><span class="number">1</span>Chiffres d'affaires par Etat</legend>
                    <div>
                        <div id="geochart-colors" style="width: 550px; height: 300px;"></div>
                    </div>
                </div>
                
                <div id="produit" style="display:none;">
                    <legend style="margin-top:0px;"><span class="number">1</span>Chiffres d'affaires par Catégorie de Produits</legend>
                    <div>
                        <div id="chart_div" style="width: 550px; height: 300px;"></div>
                    </div>
                </div>
        </form>
    </div>
         
    
    <form method="POST">
        <input type="SUBMIT" name="action" value="Vos Produits">
        <input type="SUBMIT" name="action" value="Deconnexion">
    </form> 
    </div>

</body>
