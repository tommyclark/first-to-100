package com.maddogs.game

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FirstTo100ApplicationSpec extends Specification {

    def "application starts"() {
        when: "the application starts up"
        then: "there are no errors"
        noExceptionThrown()
    }

}
