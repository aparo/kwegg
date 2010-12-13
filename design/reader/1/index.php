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
    <script src="js/jquery.jplayer.min.js"></script>
  </head>
<!-- time for some javascript. there is a reason for it to be on top-->		
<script type="text/javascript">
$().ready(function(){ 
    // variables declarations to be required
    var imgList = [];
    var audioList = new Array();
    var imgLoaded = 0;
    var audioLoaded = 0;
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
    var audio;
    var numExperios = <? echo 6; ?>;
    var currentExperio = 0;
    var isStop = false;
    var isPause = false;

    //utilities for content experience
    var timer_check = function(){
        $("#experios").doTimeout(1200, function(){
            //if(imgLoaded>=numExperios && currentExperio<numExperios && !isStop && !isPause) {
            if(currentExperio<numExperios && !isStop && !isPause) {
              if(audio==undefined || audio.currentTime>=audio.duration) {
                // show phrase
                $("#exphrase").html(json.phrases[currentExperio]);
                // play audio
                //audio = $("#experioAudio-"+currentExperio).get(0);
                audio = audioList[currentExperio];
                audio.play();
                // show image
                $("#expic-img-"+currentExperio).show();
                currentExperio++;
              }
            }
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
        $("#expic-img-"+picc).css(cssObj);
        $("#expic-img-"+picc).hide();
      }
      $("#exphrase").html("");
    };
    function togglePlay(elemid){
      var $elem = $('#'+elemid);
      $elem.stop().show().animate({'marginTop':'-175px','marginLeft':'-175px','width':'350px','height':'350px','opacity':'0'},function(){
        $(this).css({'width':'100px','height':'100px','margin-left':'-50px','margin-top':'-50px','opacity':'1','display':'none'});
        });
      //$elem.parent().append($elem);
    }
    
    // set initial css
    $('#content-scroll-up').css('max-height',contentHeight+'px');
    $('#experios').css('max-height',contentHeight+'px');
    setExpicPositions();
    
    // to get news pack
    var url = 'json.html';    
    $.getJSON(url, function(data) {
      json = data;
      $('#bar_top').html('<h1>' + json.title + '</h1>');
      $('#content-scrolled').html(json.content);
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
    $("#player_pause").hide();

    // timer utilities to load and play experios
    var start_experio = function() {
      for(var start_i=0; start_i<numExperios; start_i++) {
        $('<img>').attr({src: json.images[start_i].url, id: 'img-'+start_i}).load(function() {
            imgLoaded++;
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
        var audioUrl = json.audioUrl;
        var audElement = document.createElement('audio');
        audElement.setAttribute('src', audioUrl+'/v'+start_i+'.wav');
        audElement.load();
        audioList[start_i] = audElement;
      }
      timer_check();
    };

    //audio controls
    $("#player_play").click(function(){
      togglePlay('play');
      if(isPause) {
        audio.play();
      }
      isStop = false;
      isPause = false;
      $("#player_play").hide();
      $("#player_pause").show();
    })

    $("#player_pause").click(function(){
      togglePlay('pause');
      audio.pause();
      isPause = true;
      $("#player_pause").hide();
      $("#player_play").show();
    })

    $("#player_stop").click(function(){
      isStop = true;
      isPause = false;
      currentExperio = 0;
      setExpicPositions();
      audio.pause();
      audio.currentTime = 0;
      audio = undefined;
      $("#player_pause").hide();
      $("#player_play").show();
    })
});
</script>
  <body>
    <div class="abs" id="bar_top"></div>
    <?php
        for($i=0; $i<6; $i++) {
          echo "<div class=experio-pic id=\"expic-img-".$i."\"></div>";
        }
      ?>
    <div id="exaudio"></div>
    <div id="play-pause">
      <img id="play" src="snips/play.png" width="100" height="100" style="display:none;"/>
      <img id="pause" src="snips/pause.png" width="100" height="100" style="display:none;"/>
    </div>
    <div class="abs" id="experios">
      <div id="message"></div>
    </div>
    <div class="abs" id="bar_bottom">
      <div id="menu">
        <div id="player-container">
          <ul id="player-controls">
            <li id="player_play" class="audio_play">Play</li>
            <li id="player_pause" class="audio_pause">Pause</li>
            <li id="player_stop" class="audio_stop">Stop</li>
          </ul>
        </div>
      </div>
      <div class="phrase" id="exphrase"></div>
    </div>
    <div id="content-scroll-up">
        <div id="content-scroll-handle"> <img src="snips/round-black-down.png"> </div>
        <div id="content-scrolled"></div>
    </div>
    <div class="abs" id="content-scroll-handle-2"><div id="img2-handle"><img src="snips/round-black-up.png"></div></div>
  </body>
</html>
