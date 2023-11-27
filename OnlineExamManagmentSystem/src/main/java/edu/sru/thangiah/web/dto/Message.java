/**
 * The {@code Message} class is a data transfer object used to encapsulate
 * the data attributes of ChatGTPResponse/Request for transport between various layers of the application.
 */

package edu.sru.thangiah.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String role;
    private String content;//prompt
}