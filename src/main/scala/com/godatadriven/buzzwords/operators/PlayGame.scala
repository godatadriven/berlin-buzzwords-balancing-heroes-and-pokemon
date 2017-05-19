/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.godatadriven.buzzwords.operators

import com.godatadriven.buzzwords.definitions.{Game, Player, Team}
import org.apache.flink.api.common.functions.MapFunction

// This function simulates the outcome of the game, irl this would be an actual game
class PlayGame extends MapFunction[Team, Game] {

  private def determineScore(players: Iterable[Player]) = {
    // The skill of the player is embedded in the id :-)
    val playerSkill = players.map(player => (player.player % 100) / 100).sum

    // Add a bit of randomness
    playerSkill + Math.random() / 10
  }

  override def map(team: Team): Game = {
    val scoreA = determineScore(team.firstTeam._1)
    val scoreB = determineScore(team.secondTeam._1)

    val flip = Math.random() < (scoreA / (scoreA + scoreB))

    Game(
      // Winning team
      if (flip) team.firstTeam else team.secondTeam,

      // Losing team
      if (flip) team.secondTeam else team.firstTeam
    )
  }
}