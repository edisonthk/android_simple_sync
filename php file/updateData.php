<?php
//updateData and retrieve data
$accounts = mysql_connect("localhost","user123","1234")
or die(mysql_error());

mysql_select_db("user123",$accounts);

if(isset($_POST['text'])&&isset($_POST['column'])){
$text = $_POST['text'];
$column = $_POST['column'];
$result = mysql_query( "UPDATE blank SET $column = '$text'");

}else{
	$result['column'] = "not found";
	echo json_encode($result);
}
?>