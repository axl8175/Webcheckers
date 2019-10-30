 <div class="navigation">
  <#if PlayerServices??>
    <a href="/">my home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">sign out ${PlayerServices.Id()}</a>
    </form>
  <#else>
    <a href="/signin">sign in</a>
  </#if>
 </div>
