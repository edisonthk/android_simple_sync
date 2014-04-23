
<?php
//retrieve only
$accounts = mysql_connect("localhost","user123","1234")
or die(mysql_error());

mysql_select_db("user123",$accounts);

$result = mysql_query( "SELECT * FROM blank");

if($row = mysql_fetch_array($result)){
echo json_encode($row);
}else{
echo 'error';
}
?>
