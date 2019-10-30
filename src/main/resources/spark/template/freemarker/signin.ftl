<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <#-- Provide a navigation bar -->
  <div class="navigation">
	<a href="/">home</a>
  </div>

  <div class="body">

    <#-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <#if Error??>
    <div id="error" class="ERROR">${Error}</div>
    </#if>

    <form action="./signin" method="POST">
        Enter a User Name:
        <br/>
        <input name="username" type="text" />
        <br/><br/>
        <input type="submit" value="Sign in" />
    </form>


  </div>

</div>
</body>

</html>
