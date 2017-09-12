function  validate_data(){
    var check=0;
    var qcheck;
    var formname = "coursesurvey";
    qcheck = document.forms[formname].elements['comment1'].value;

    if (qcheck.length!=5 || isNaN(qcheck)){
        alert("Please enter the correct CRN number");
        document.forms[formname].elements['comment1'].focus();
        return;
    };

    document.forms[formname].submit();

    /*
   qcheck = document.forms[formname].elements['select2'];
   if (qcheck[0].checked!=true){
      if (qcheck[1].checked!=true){
         if (qcheck[2].checked!=true){
            if (qcheck[3].checked!=true){
			   if (qcheck[4].checked!=true){
				  if (qcheck[5].checked!=true){
				     if (qcheck[6].checked!=true){
	                    alert("Question 1 can not be blank");
                        document.forms[formname].elements['select2'][0].focus();
                        return;
					 }
				  }
			   }
            }
         }
      }
   };
   */
}

function validate_length(formname, varname, maxlen, display_variable){
    var qcheck = document[formname].elements[varname].value;
    if (qcheck!=null && qcheck.length>maxlen){
        alert(display_variable+" is over the available size "+maxlen+".");
        document[formname].elements[varname].focus();
    }
}

//********** Date format: MM/DD/YYYY **********
function datevalidate(strDate){
    var b1, b2, b3, b4, i, j, k, bb, ib2, ib1, ib0;
    b1 = document.getElementById(strDate).value;
    bb = true;
    //date format: MM/DD/YY  b[0]=MM, b[1]=DD, b[2]=YY
    b3 = /[0-9]{1,2}\/[0-9]{1,2}\/[0-9]{4}/ ;
    if (!b3.test(b1) && b1.length>0){
        alert ("Error! Date Format: MM/DD/YYYY");
        document.getElementById(strDate).focus();
        return false;
    }

    b2  = b1.split("/");
    ib0 = parseInt(b2[0],10);
    ib1 = parseInt(b2[1],10);
    ib2 = parseInt(b2[2],10);
    b4  = b2.length;

    if (b1!=null && b1.length>0){
        if (isNaN(ib2) || ib2<2005 || ib2>2020){
            alert("Error! Year must be between 2005 and 2020");
        }
        else {
            if (ib0<1 || ib0>12){
                alert("Error! Month must be between 1 and 12");
            }
            else {
                if (parseInt(b2[1],10)<1 || parseInt(b2[1],10)>31){
                    alert("Error! Day is between 1 and 31");
                }
                else {
                    k = ib2 % 4;
                    if ((ib0==4 || ib0==6 || ib0==9 || ib0==11) && ib1>30){
                        alert("Error! Day is between 1 and 30");
                    }
                    else {
                        if (k>0 && ib1>28 && ib0==2){
                            alert("Error! Day is between 1 and 28");
                        }
                        else {
                            if (k==0 && ib1>29 && ib0==2){
                                alert("Error! Day is between 1 and 29");
                            }
                            else {
                                bb = false;
                            }
                        }
                    }
                }
            }
        }
        if (bb){
            document.getElementById(strDate).focus();
            return false;
        }
    }
    return true;
}
// *