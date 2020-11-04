/*
 * Copyright (C) 2020  Ľuboš Kozmon <https://www.elkozmon.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elkozmon.zoonavigator.core.curator.syntax

import cats.effect.Async
import org.apache.curator.framework.api.BackgroundPathAndBytesable
import scala.concurrent.ExecutionContext
import org.apache.curator.framework.api.CuratorEvent
import scala.language.implicitConversions
import com.elkozmon.zoonavigator.core.utils.CommonUtils._
import org.apache.curator.framework.api.Backgroundable
import org.apache.curator.framework.api.ErrorListenerMultiTransactionMain
import org.apache.curator.framework.api.BackgroundPathable
import org.apache.curator.framework.api.GetDataBuilder

trait AsyncOps {

  implicit def toPathableF[F[_]: Async](bp: BackgroundPathable[_])(implicit ec: ExecutionContext): PathableF[F] =
    path =>
      Async[F].async[CuratorEvent] { callback =>
        bp.inBackground(newEventCallback(callback), ec)
          .withUnhandledErrorListener(newErrorListener(callback))
          .forPath(path)
          .discard()
      }

  implicit def toPathableF[F[_]: Async](
      bp: BackgroundPathAndBytesable[_]
  )(implicit ec: ExecutionContext): PathableF[F] =
    path =>
      Async[F].async[CuratorEvent] { callback =>
        bp.inBackground(newEventCallback(callback), ec)
          .withUnhandledErrorListener(newErrorListener(callback))
          .forPath(path)
          .discard()
      }

  implicit def toPathAndBytesableF[F[_]: Async](
      bp: BackgroundPathAndBytesable[_]
  )(implicit ec: ExecutionContext): PathAndBytesableF[F] =
    (path, bytes) =>
      Async[F].async[CuratorEvent] { callback =>
        bp.inBackground(newEventCallback(callback), ec)
          .withUnhandledErrorListener(newErrorListener(callback))
          .forPath(path, bytes)
          .discard()
      }

  implicit def toTransactionF[F[_]: Async](
      bp: Backgroundable[ErrorListenerMultiTransactionMain]
  )(implicit ec: ExecutionContext): TransactionF[F] =
    ops =>
      Async[F].async[CuratorEvent] { callback =>
        bp.inBackground(newEventCallback(callback), ec)
          .withUnhandledErrorListener(newErrorListener(callback))
          .forOperations(ops: _*)
          .discard()
      }
}
