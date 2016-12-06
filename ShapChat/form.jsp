<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="style.css">
  <title>Survey</title>
  <script type="text/javascript">
    //check all fields are filled out
    function checkFields() {
      var claim = document.getElementById("claim").value;
      var evidence = document.getElementById("evidence").value;
      var name = document.getElementById("name").value;
      if(claim.length != 0 && evidence.length != 0 && name.length != 0) {
        return true;
      }
      alert("Please fill out all fields!");
      return false;
    }
  </script>
</head>
<body>
  <div id="heading">
    <h1>Quick Survey</h1>
    <p>Please fill out all fields below and Submit once finished.</p>
  </div>
  <div id="form">
    <form method="POST" action="http://localhost:9999/assignment6/FormHandler" onsubmit="return checkFields();">
    <!--only prompt name if not in session already-->
    <% if (session.getAttribute("name") == null) { %>
        Name: 
        <br />
        <input type="text" name="name" id="name" />
        <br />
        <br />
        Password: 
        <br />
        <input type="password" name="pass" id="pass" />
        <br />
        <br />
    <% } %>

      
      Claim: 
      <br />
      <input type="text" name="claim" id="claim" />
      <input type="hidden" name="reset" value="false" />
      <br />
      <br />
      Evidence: 
      <br />
      <textarea name="evidence" cols="40" id="evidence" rows="5"></textarea>
      <br />
      <br />
      <input type="submit" class="button" value="Submit" />
    </form>
  </div>
</body>
</html>