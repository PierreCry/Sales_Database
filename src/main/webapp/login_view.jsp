<%-- 
    Document   : login_view
    Author     : Diego
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/util.css">
        <link rel="stylesheet" type="text/css" href="css/main.css">
        <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
    </head>
    <script>

        function myFunction() {
            var x = document.getElementById("myInput");
            if (x.type === "password") {
                x.type = "text";
            } else {
                x.type = "password";
            }
        }


    </script>
    <body>
        <div class="limiter">
            <div class="container-login100">
                <div class="wrap-login100 p-l-85 p-r-85 p-t-55 p-b-55">
                    <form method="POST" class="login100-form validate-form flex-sb flex-w">
                        <span class="login100-form-title p-b-32" style="color:white;">
                            Se connecter
                        </span>
                        <span class="txt1 p-b-11" style="color:white">
                            Adresse mail
                        </span>
                        <div class="wrap-input100 validate-input m-b-36" data-validate = "Username is required">
                            <input class="input100" type="text" name="log" >
                            <span class="focus-input100"></span>
                        </div>
                        <span class="txt1 p-b-11" style="color:white;">
                            Mot de passe
                        </span>
                        <div class="wrap-input100 validate-input m-b-12" data-validate = "Password is required">
                            <span class="btn-show-pass">
                                <i class="fa fa-eye"></i>
                            </span>
                            <input class="input100" type="password" name="mdp" id="myInput">
                            <span class="focus-input100"></span>
                        </div>
                        <div class="flex-sb-m w-full p-b-48">
                            <div>
                                <span class="txt3 p-b-11" style="color:white;">
                                    Montrer le mot de passe
                                    <input type="checkbox" onclick="myFunction()">
                                </span>

                            </div>
                        </div>
                        <div class="container-login100-form-btn">
                            <button class="login100-form-btn" name="action" value="Connexion" type="SUBMIT" style="background-color: white; color:#1abc9c;">
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
