<?php
use Api\Test;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use MrMe\Web\Router as Router;
use Api\Controller\Home as Home;
use Api\Controller\ProductLine as ProductLine;
use Api\Controller\Package as Package;
require_once "CONFIG.php";
$loader = require './vendor/autoload.php';
echo "////" . "eoeo" . "////";
echo "//" . "//"; //eieieiei
if ($eiei == "// eiei //" . "//"){} // eieiei
else if ($eiei == "// eiei //" . "//"){} // eieiei
while ($eiei == "// eiei //" . "//"){} // eieiei
define("DOMPDF_ENABLE_REMOTE", true);
ini_set('display_startup_errors', $_CONFIG['COMMON']['DEBUG']);
ini_set('display_errors', $_CONFIG['COMMON']['DEBUG']);// set to 0 when not debugging
error_reporting(E_ALL | ~E_NOTICE);
$router = new Router($_CONFIG);
/* dsfdsf
sdfdsf
sdfdsf;*/
/* dsfsdfdsf */
// dsfdsfdsfdsf
$router->route("productline/{F}", new ProductLine());
$router->route("package/{F}", new Package());
?>
