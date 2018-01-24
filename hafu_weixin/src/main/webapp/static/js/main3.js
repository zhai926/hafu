/**
 * main3.js
 * http://www.codrops.com
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * Copyright 2014, Codrops
 * http://www.codrops.com
 */
(function() {

	var bodyEl = document.body,
		content = document.querySelector( '.content-wrap' ),
		openbtn = document.getElementById( 'open-button' ),
		closebtn = document.getElementById( 'close-button' ),
		isOpen = false,

		morphEl = document.getElementById( 'morph-shape' );
		if(morphEl!=null){
			var s = Snap( morphEl.querySelector('svg') ),
			path = s.select( 'path' ),
			initialPath = $("#morph-shape svg path").attr('d'),
			pathOpen = morphEl.getAttribute( 'data-morph-open' ),
			isAnimating = false;
			init();
			
			//if(swipe) detectswipe();
		}
		

	function init() {
		initEvents();
	}

	function initEvents() {
		if(openbtn!=null){
			openbtn.addEventListener( 'click', toggleMenu );
			if( closebtn ) {
				closebtn.addEventListener( 'click', toggleMenu );
			}

			// close the menu element if the target it´s not the menu element or one of its descendants..
			content.addEventListener( 'click', function(ev) {
				var target = ev.target;
				if( isOpen && target !== openbtn ) {
					toggleMenu();
				}
			} );
		}
	}

	function toggleMenu() {
		if( isAnimating ) return false;
		isAnimating = true;
		if( isOpen ) {
			classie.remove( bodyEl, 'show-menu' );
			// animate path
			setTimeout( function() {
				// reset path
				path.attr( 'd', initialPath );
				isAnimating = false; 
			}, 300 );
		}
		else {
			classie.add( bodyEl, 'show-menu' );
			// animate path
			path.animate( { 'path' : pathOpen }, 400, mina.easeinout, function() { isAnimating = false; } );
		}
		isOpen = !isOpen;
	}
	
	function detectswipe(el,func) {
		  swipe_det = new Object();
		  swipe_det.sX = 0; swipe_det.sY = 0; swipe_det.eX = 0; swipe_det.eY = 0;
		  var min_x = 100;  //min x swipe for horizontal swipe
		  var max_x = 100;  //max x difference for vertical swipe
		  var min_y = 50;  //min y swipe for vertical swipe
		  var max_y = 60;  //max y difference for horizontal swipe
		  var direc = "";
		  ele =$(document)[0];
		  ele.addEventListener('touchstart',function(e){
		    var t = e.touches[0];
		    swipe_det.sX = t.screenX; 
		    swipe_det.sY = t.screenY;
		  },false);
		  ele.addEventListener('touchmove',function(e){
		   // e.preventDefault();
		    var t = e.touches[0];
		    swipe_det.eX = t.screenX; 
		    swipe_det.eY = t.screenY;    
		  },false);
		  ele.addEventListener('touchend',function(e){
		    //horizontal detection
		    if ((((swipe_det.eX - min_x > swipe_det.sX) || (swipe_det.eX + min_x < swipe_det.sX)) && ((swipe_det.eY < swipe_det.sY + max_y) && (swipe_det.sY > swipe_det.eY - max_y) && (swipe_det.eX > 0)))) {
		      if(swipe_det.eX > swipe_det.sX) direc = "r";
		      else direc = "l";
		    }
		    //vertical detection
		    else if ((((swipe_det.eY - min_y > swipe_det.sY) || (swipe_det.eY + min_y < swipe_det.sY)) && ((swipe_det.eX < swipe_det.sX + max_x) && (swipe_det.sX > swipe_det.eX - max_x) && (swipe_det.eY > 0)))) {
		      if(swipe_det.eY > swipe_det.sY) direc = "d";
		      else direc = "u";
		    }

		    if (direc=="l"||direc=="r") {
		    	toggleMenu();
		    }else if(direc=="u"){
		    	console.log("up");
		    }else if(direc=="d"){
		    	console.log("down");
		    }
		    direc = "";
		    swipe_det.sX = 0; swipe_det.sY = 0; swipe_det.eX = 0; swipe_det.eY = 0;
		  },false);  
		}

})();