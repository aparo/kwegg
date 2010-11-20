<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:fb="http://www.facebook.com/2008/fbml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="title" content="<?=$title?>" /> 
    <meta name="description" content="<?=$description?>" /> 
    <link rel="icon" href="favicon.ico">
    <title>Reader Display Test Page</title>
    <link rel="stylesheet" type="text/css" href="screen.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery-ui.min.js"></script>
    <script src="js/jquery.dotimeout.min.js"></script>
  </head>
<!-- time for some javascript. there is a reason for it to be on top-->		
<script type="text/javascript">
$().ready(function(){ 
    // variables declarations to be required
    var imgList = [];
    var audioList = [];
    var loaded = 0;
    var json='';
    var docHeight = $(document).height();
    var docWidth = $(document).width();
    var rightStreamWidth = 300;
    var topBarHeight = 60;
    var bottomBarHeight = 120;
    var contentHeight = docHeight - topBarHeight - bottomBarHeight -32;
    var contentHeight2 = contentHeight - 29;
    var maxPicWidth = docWidth;
    var maxPicHeight = 200;

    //utilities for content experience
    var timer_check = function(){
        $("#experios").doTimeout(1000, function(){
            timer_check();
        });
    };
    var show_content = function(){
       $("#content-scroll-handle-2").hide();
       $("#content-scroll-up").show("slide", { direction: "down" }, 500, function(){
       });
    };
    var hide_content = function(){
       $("#content-scroll-up").hide("slide", { direction: "down" }, 500, function(){
         $("#content-scroll-handle-2").show();
       });
    };
    var setExpicPositions = function(){
      for(var picc=0; picc < <?php echo 6; ?>; picc++) {
        var top = Math.floor(60 + ((contentHeight-200) * Math.random()));
        var left = Math.floor((docWidth-100) * Math.random());
        var cssObj = {
          'position' : 'absolute',
          'left': left + 'px',
          'top': top + 'px'
        };
        $("#expic-"+picc).css(cssObj);
        //$("#expic-"+picc).hide();
      }
    };
    
    // set initial css
    $('#content-scroll-up').css('max-height',contentHeight+'px');
    $('#experios').css('max-height',contentHeight+'px');
    setExpicPositions();
    
    // to get news pack
    var url = 'json.html';    
    $.getJSON(url, function(data) {
      json = data;
      $('#bar_top').html('<h1>' + data.title + '</h1>');
      $('#content-scrolled').html(data.content);
      //setExperiosPosition();
      start_experio();
    });
    // to bind events
    $('#content-scroll-handle-2').click(function() {
      show_content();
    });
    $('#content-scroll-handle').click(function() {
      hide_content();
    });
    // initial settings
    $("#content-scroll-up").hide();

    // timer utilities to load and play experios
    var start_experio = function() {
      for(var start_i=0; start_i<6; start_i++) {
        $('<img>').attr({src: json.newsPack[start_i].imageUrl, id: start_i}).load(function(start_i) {
            loaded++;
            var pwidth = $(this).attr('width'); 
            var pheight = $(this).attr('height');
            if(pheight > maxPicHeight) {
               pwidth = parseInt(pwidth*maxPicHeight/pheight);
               pheight = maxPicHeight;
             }
            if(pwidth > maxPicWidth) {
               pheight = parseInt(pheight*maxPicWidth/pwidth);
               pwidth = maxPicWidth;
             }
            $(this).attr({height: pheight, width: pwidth});
            $('#expic-'+$(this).attr('id')).html($(this));
          });
      }
    };
});
</script>
  <body>
    <div class="abs" id="bar_top"></div>
    <?php
        for($i=0; $i<6; $i++) {
          echo "<div id=\"expic-".$i."\"></div>";
        }
      ?>
    <div class="abs" id="experios"></div>
    <div class="abs" id="bar_bottom">
      <div id="menu">
        <h3>testing menu for testing what needs to be test</h3>
      </div>
      yup of the yups is the yup
    </div>
    <div id="content-scroll-up">
        <div id="content-scroll-handle"> <img src="snips/round-black-down.png"> </div>
        <div id="content-scrolled"></div>
    </div>
    <div class="abs" id="content-scroll-handle-2"><div id="img2-handle"><img src="snips/round-black-up.png"></div></div>
  </body>
</html>
