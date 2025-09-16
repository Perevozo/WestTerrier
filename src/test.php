<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<?php
$filename = "F:\Projects\WestTerrier\WestsParents.txt";
$lines = file($filename, FILE_IGNORE_NEW_LINES);

foreach ($lines as $line) {
    echo $line . "<br>";
}

echo '<table border="1">';

    // Первые два имени с rowspan=2
    for ($i = 0; $i < 2; $i++) {
    echo '<tr>';
        echo '<td rowspan="2">' . $line[$i] . '</td>';
        echo '</tr>';
    }

    // Следующие 4 имени с rowspan=4
    for ($i = 2; $i < 6; $i++) {
    echo '<tr>';
        echo '<td rowspan="4">' . $line[$i] . '</td>';
        echo '</tr>';
    }

    // Следующие 8 имен с rowspan=8
    for ($i = 6; $i < 14; $i++) {
    echo '<tr>';
        echo '<td rowspan="8">' . $line[$i] . '</td>';
        echo '</tr>';
    }

    echo '</table>';
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
</html>