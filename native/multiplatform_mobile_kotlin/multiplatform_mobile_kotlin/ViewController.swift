//
//  ViewController.swift
//  multiplatform_mobile_kotlin
//
//  Created by Олег Мушенко on 9/17/19.
//  Copyright © 2019 Ele. All rights reserved.
//
import UIKit
import SharedCode

class ViewController: UIViewController {
    @IBOutlet weak var message: UITextField!
    @IBOutlet weak var labelTest: UILabel!
    var db:ArticlesDb!
    override func viewDidLoad() {
        super.viewDidLoad()
        db = Db().instance
        db.articlesQueries.insertPlayers(article_id: 12, article_name: "Hello", article_text: "String")
        print(db.articlesQueries.selectAll().executeAsList())
        //let label = UILabel(frame: CGRect(x: 0, y: 0, width: 300, height: 21))
        //label.center = CGPoint(x: 160, y: 285)
       // label.textAlignment = .center
       // label.font = label.font.withSize(25)
//        ArticlesLoader().loadFirst(
//            successCallback:{ [weak self] data in
//                self?.message.text = data[0].component3()
//                self?.labelTest.text = data[0].component3()
//                print(data[0].component3())
//            
//            }, errorCallback: { [weak self] error in
//                self?.message.text = error.message
//                self?.labelTest.text = error.message
//                self?.labelTest.textColor = UIColor.red
//                print(error.message)
//        
//        })
       // view.addSubview(label)
    }
   
}

