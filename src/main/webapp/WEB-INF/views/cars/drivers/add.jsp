<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Driver to Car</title>
</head>
<body>

<div style="text-align: center;"><h1>Add Driver to Car</h1>
<form method="post" action="${pageContext.request.contextPath}/drivers/add">
    Please, provide driver's id: <label>
    <input type="number" name = driverId> <br>
</label>
    Please, provide car's id: <label>
    <input type="number" name = carId> <br>
</label>
    <button type="submit">Add</button> <br>
</form>
</div>
</body>
</html>
