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

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 300, height: 21))
        label.center = CGPoint(x: 160, y: 285)
        label.textAlignment = .center
        label.font = label.font.withSize(25)
        GitHubApiClient(githubUserName: "username", githubPassword: "password").repos(
            successCallback:{ [weak self] repos in
              
            }, errorCallback: { [weak self] error in
               
        })
        view.addSubview(label)
    }


}

