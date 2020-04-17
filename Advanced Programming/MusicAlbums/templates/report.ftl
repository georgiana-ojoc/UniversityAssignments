<html>
<head>
    <title>HTML report</title>
</head>
    <body>
        <h1>Artists ranked by albums</h1>
        <table style="width:50%">
            <tr>
                <th style="text-align:center">Rank</th>
                <th style="text-align:center">Artist</th>
                <th style="text-align:center">Country</th>
                <th style="text-align:center">Score</th>
            </tr>
            <#foreach artist in artists>
                <tr>
                    <td style="text-align:center">${artist.rank}</td>
                    <td style="text-align:center">${artist.name}</td>
                    <td style="text-align:center">${artist.country}</td>
                    <td style="text-align:center">${artist.score}</td>
                </tr>
            </#foreach>
        </table>
    </body>
</html>