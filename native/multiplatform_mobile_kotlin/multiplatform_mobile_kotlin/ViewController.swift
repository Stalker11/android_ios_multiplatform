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
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 300, height: 21))
        label.center = CGPoint(x: 160, y: 285)
        label.textAlignment = .center
        label.font = label.font.withSize(25)
        GitHubApiClient(login: "T##String", password: "T##String").repos(
            successCallback:{ [weak self] repos in
                //self?.message.text = repos
            
            }, errorCallback: { [weak self] error in
                self?.message.text = error.message
                self?.labelTest.text = error.message
                self?.labelTest.textColor = UIColor.red
        
        })
        view.addSubview(label)
    }
   
}
class Child:GitHubApiClient{

}
class CCC:TestKotlinMP{
    
}
