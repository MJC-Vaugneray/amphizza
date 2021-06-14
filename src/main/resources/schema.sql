CREATE TABLE IF NOT EXISTS pizza_order (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     status VARCHAR(255) NOT NULL,
                                     pizza_type VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS pizza (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           pizza_type VARCHAR(255) NOT NULL,
                                           doing_number INT,
                                           ready_number INT,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)  ENGINE=INNODB;