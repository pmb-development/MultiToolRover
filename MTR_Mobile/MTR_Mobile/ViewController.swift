//
//  ViewController.swift
//  MTR_Mobile
//
//  Created by Emil Berger on 16.04.19.
//  Copyright © 2019 PMB Development. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController {
    
    enum ConnectionError: Error {
        case ipEmpty
    }
    
    var client: TCPClient = TCPClient(address: "", port: 1804)
    var connected: Bool = false
    
    @IBOutlet weak var ipAddressTF: UITextField!
    
    @IBAction func connectTapped(_ sender: UIButton) {
        do {
            if (ipAddressTF.text!.isEmpty) {
                throw ConnectionError.ipEmpty
            }
            client = TCPClient(address: ipAddressTF.text!, port: 1804)
            let result = client.connect(timeout: 3)
            if (result.isFailure) {
                throw result.error!
            }
            connected = true
        } catch (ConnectionError.ipEmpty) {
            let alert = UIAlertController(title: "IP Address Error", message: "IP Address Field must no be empty!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            self.present(alert, animated: true)
        } catch (SocketError.connectionTimeout) {
            let alert = UIAlertController(title: "Connection Error", message: "Connection timed out!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            self.present(alert, animated: true)
        } catch (SocketError.queryFailed) {
            let alert = UIAlertController(title: "Connection Error", message: "Query failed!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        } catch (SocketError.connectionClosed) {
            let alert = UIAlertController(title: "Connection Error", message: "Connection closed!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        } catch let unknownErrors {
            print(unknownErrors)
        }
    }
    
    @IBAction func disconnectTapped(_ sender: UIButton) {
        client.close()
        connected = false
    }
    
    @IBAction func velocityValueChanged(_ sender: Any) {
    }
    
    @IBAction func speedModeSwitch(_ sender: Any) {
    }
    
    @IBAction func moveForwardTapped(_ sender: Any) {
    }
    
    @IBAction func moveBackwardTapped(_ sender: Any) {
    }
    
    @IBAction func moveRightTapped(_ sender: Any) {
    }
    
    @IBAction func moveLeftTapped(_ sender: Any) {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }


}

