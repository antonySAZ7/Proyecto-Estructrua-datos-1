(defun fahrenheit-a-celsius(f)
  (/(* 5.0 (- f 32 ))9.0))


(defun main ()
  (format t "Ingrese la temperatura en fahrenheit: ")
  (let ((temp (read)))
    (format t "~a°F en celsius es : ~F~%" temp (fahrenheit-a-celsius temp))))

(main)
  
